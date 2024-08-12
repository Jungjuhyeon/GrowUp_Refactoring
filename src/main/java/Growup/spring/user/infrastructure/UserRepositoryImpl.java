package Growup.spring.user.infrastructure;

import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.user.application.UserRepository;
import Growup.spring.user.domain.Enum.UserState;
import Growup.spring.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public Boolean existsByNickName(String nickName){
        return userJpaRepository.existsByNickName(nickName);
    }


    @Override
    public Boolean existsByEmailAndStatus(String email, UserState state){
        return userJpaRepository.existsByEmailAndStatus(email,state);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);

    }

    @Override
    public User findByEmailAndStatus(String email){
        return userJpaRepository.findByEmailAndStatus(email, UserState.ACTIVE)
                .or(() -> userJpaRepository.findByEmailAndStatus(email, UserState.NONACTIVE))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND));
    }
    @Override
    public User findById(Long id){
        return userJpaRepository.findById(id).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

}
