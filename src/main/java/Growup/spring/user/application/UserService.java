package Growup.spring.user.application;

import Growup.spring.config.S3.S3Uploader;
import Growup.spring.config.jwt.util.JwtUtil;
import Growup.spring.config.redis.util.RedisUtil;
import Growup.spring.constant.handler.EmailHandler;
import Growup.spring.constant.handler.JwtHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.user.converter.UserConverter;
import Growup.spring.user.domain.Enum.UserState;
import Growup.spring.email.service.EmailService;
import Growup.spring.user.dto.UserDtoReq;
import Growup.spring.user.domain.User;
import Growup.spring.user.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;




@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtUtil jwtUtil;
    public final RedisUtil redisUtil;
    public final EmailService emailService;
    public final S3Uploader s3Uploader;

    private static final String RT = "RT:";

    /**
     *  회원가입
     */
    @Transactional
    public UserDtoRes.userRegisterRes signUp(UserDtoReq.userRegisterReq request){

        // 닉네임 중복 확인
        checkNickDuplication(request.getNickName());

        // 이메일 중복 확인( 활동회원인지, 미인증 회원인지)
        if (userRepository.existsByEmailAndStatus(request.getEmail(),UserState.ACTIVE)||
                (userRepository.existsByEmailAndStatus(request.getEmail(),UserState.NONACTIVE))){
            throw new UserHandler(ErrorStatus.USER_EMAIL_DUPLICATE);
        }
        // 두 비밀번호 일치성 확인
        checkPassword(request.getPassword(),request.getPasswordCheck());
        //비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        //유저 생성
        User newUser = User.create(request);

        return UserConverter.userDtoRes(userRepository.save(newUser));
    }

    /**
     *  회원가입 인증완료 확인
     */
    @Transactional
    public void signUpAuth(String certificationNumber,String email){
        //인증이 유효한지
        User user = verifyEmail(certificationNumber, email);
        //인증완료시 활성화
        user.changeStatus();
    }

    /**
     *  로그인
     */
    @Transactional
    public UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request) {
        //해당 Email로 아이디 찾기 - 아이디 불일치(ACTIVE,NONACTIVE)
        User user = userRepository.findByEmailAndStatus(request.getEmail());

        // 인증 확인
        if(user.isActive()){throw new EmailHandler(ErrorStatus._UNAUTHORIZED);}
        //비밀번호 일치성 확인
        EncodePasswordMatch(request.getPassword(), user.getPassword());
        //토큰 생성
        String accessToken = jwtUtil.createAccessToken(user.getId(),user.getRole().name());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        // RefreshToken을 redis에 저장
        redisUtil.setData(RT + user.getId(), refreshToken, jwtUtil.REFRESH_TOKEN_VALID_TIME);

        return UserConverter.userLoginRes(user,accessToken,refreshToken);
    }

    /**
     * AccessToken 만료시 재발급
     */
    public String inVaildToken(String refreshToken) {
        //pk 추출
        Long userId = jwtUtil.getIdFromToken(refreshToken);
        //pk로 아이디 찾기
        User findUser = userRepository.findById(userId);
        //토큰 생성
        String refreshTokenInRedis = redisUtil.getData(RT + userId);
        //토큰이 일치하지 않을떄
        if(!refreshToken.equals(refreshTokenInRedis)) {
            throw new JwtHandler(ErrorStatus.JWT_REFRESHTOKEN_NOT_MATCH);
        }
        return jwtUtil.createAccessToken(userId,findUser.getRole().name());
    }

    /**
     * 비밀번호 재설정 인증완료 확인 후-accessToken 발급
     */
    public String passwordAuthToken(String certificationNumber,String email){
        User user = verifyEmail(certificationNumber, email);
        return jwtUtil.createAccessToken(user.getId(),String.valueOf(user.getRole()));
    }
    /**
     * 패스워드 재설정
     */
    @Transactional
    public User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId){
        //유저 아이디 찾기(유저 객체 조회)
        User user = userRepository.findById(userId);
        //두게의 비밀번호 일치한지 확인
        checkPassword(request.getPassword(),request.getPasswordCheck());
        //기존 비밀번호와 일치한지 확인
        EncodePasswordMatch(request.getPassword(), user.getPassword());
        //비밀번호 암호화 (저장)
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        user.changePassword(request.getPassword());
        return userRepository.save(user);
    }

    //이메일 인증 전송
    public void sendEmailAuth(String email,String text){
        //해당 Email로 아이디 찾기 - (ACTIVE,NONACTIVE)
        User user = userRepository.findByEmailAndStatus(email);
        // ACTIVE 또는 NONACTIVE 상태인 경우에만 이메일 전송
        emailService.sendMail(email, text);
    }

    //이메일 인증이 유효
    public User verifyEmail(String certificationNumber, String email) {

        //해당 Email로 아이디 찾기 - (ACTIVE,NONACTIVE)
        User user = userRepository.findByEmailAndStatus(email);

        String authCode = redisUtil.getData("AuthCode_"+email);

        if(!certificationNumber.equals(authCode)){
            throw new EmailHandler(ErrorStatus.EMAIL_VERIFY_ERROR);
        }
        return user;
    }

    //로그아웃
    @Transactional
    public void logout(String accessToken,Long userId) {
        Long expiration = jwtUtil.getExpiration(accessToken);

        redisUtil.deleteDate(RT + userId);

        redisUtil.setData(accessToken,"logout",expiration/1000L);

    }

    //이메일 변경
    @Transactional
    public void emailChange(String email,Long userId){

        User user = userRepository.findById(userId);

        user.changeEmail(email);

        userRepository.save(user);
    }

    /**
     *
     * 현재비밀번호 같은지 체크후 인증번호 발송
     */
    public void currentPasswordCheckReq(Long userId, UserDtoReq.currentPasswordCheckReq request) {
        User user = userRepository.findById(userId);

        //기존 비밀번호와 일치한지 확인
        EncodePasswordMatch(request.getCurrentPwd(), user.getPassword());
    }

    /**
     *  회원탈퇴
     */
    @Transactional
    public void withdraw(Long userId, String currentPwd) {
        User user = userRepository.findById(userId);

        //기존 비밀번호와 일치한지 확인
        EncodePasswordMatch(currentPwd, user.getPassword());

        user.withdraw();

        userRepository.save(user);
    }
    /**
     *  마이페이지(닉넴,이멜,이미지) 조회
     */
    public UserDtoRes.infoRes info(Long userId) {
        User user = userRepository.findById(userId);
        return UserConverter.info(user);
    }

    /**
     *  프로필 이미지 변경
     */
    @Transactional
    public User photoChange(MultipartFile photoImage, Long userId) {
        User user = userRepository.findById(userId);

        String fileName = "";
        if (photoImage != null) {
            try {
                fileName = s3Uploader.upload(photoImage, "profileImage");
            } catch (IOException e) {
                throw new UserHandler(ErrorStatus.FILE_CHANGE_ERROR);
            }
            user.changeProfile(fileName);
        }
        return userRepository.save(user);
    }

    /**
     *  닉네임 변경
     */
    @Transactional
    public void changeNickname(String nickName,Long userId) {
        User user = userRepository.findById(userId);
        //닉네임 중복확인
        checkNickDuplication(nickName);

        user.changeNickname(nickName);

        userRepository.save(user);

    }

    // 닉네임 중복 확인
    public void checkNickDuplication(String nickName){
        if (userRepository.existsByNickName(nickName)){
            throw new UserHandler(ErrorStatus.USER_NICKNAME_ERROR);
        }
    }

    //암호화 비밀번호 불일치 확인
    public void EncodePasswordMatch(String RequestPassword,String password){
        if (!passwordEncoder.matches(RequestPassword, password)) {
            throw new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND);
        }
    }
    //두게의 비밀번호 일치한지 확인
    public void checkPassword(String password,String checkPassword) {
        if (!password.equals(checkPassword)) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }
    }

}
