package Growup.spring.calender.application;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.domain.Calender;

import Growup.spring.user.application.UserRepository;
import Growup.spring.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CalenderService {
    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;


    //캘린더 등록
    @Transactional
    public Calender calenderEnroll(Long userId, CalenderDtoReq.calenderEnroll request){
        userRepository.findById(userId);
        return calenderRepository.save(Calender.create(userId,request));
    }

    //캘린더 특정 날짜 목록 조회
    public CalenderDtoRes.calenderInquiryResultRes calenderInquiry(LocalDate day, Long userId) {
        userRepository.findById(userId);

        //db에서 가져올때, 필터를 걸어야겠다.
        List<CalenderDtoRes.calenderInquiryRes> calenderInquiryResList = calenderRepository.findDayByUserId(userId,day);

        return CalenderConverter.calenderInquiryResultRes(userId,day,calenderInquiryResList);
    }

    //캘린더 해당 달 전체 조회
    public CalenderDtoRes.calenderMonthInquiryResultRes calenderMonthInquiry(Long userId, YearMonth day){
        User user = userRepository.findById(userId);

        // 쿼리스트링에서 전달받은 날짜를 LocalDate로 파싱
        LocalDate parseDay = LocalDate.parse(day + "-01");

        // 해당 월의 시작일과 종료일 계산
        LocalDate startOfMonth = parseDay.withDayOfMonth(1);
        LocalDate endOfMonth = YearMonth.from(parseDay).atEndOfMonth();

        //해당 달의 캘린더를 가져온다.
        List<Calender> calendarList = calenderRepository.findMonthDayByUserId(startOfMonth,endOfMonth,userId);


        // 날짜별로 데이터를 그룹화하고 DTO로 변환
        Map<LocalDate, List<CalenderDtoRes.calenderInquiryRes>> groupedData = calendarList.stream()
                .collect(Collectors.groupingBy(
                        calendar -> calendar.getDay(), // 그룹화 기준을 람다식으로 설정
                        Collectors.mapping(
                                calendar -> CalenderConverter.calenderInquiryRes(calendar), // DTO 변환을 람다식으로 설정
                                Collectors.toList() // 변환된 DTO 리스트로 수집
                        )
                ));

        // 그룹화된 데이터를 CalenderMonthInquiryRes로 변환
        List<CalenderDtoRes.calenderMonthInquiryRes> calenderMonthInquiryResList  = groupedData.entrySet().stream()
                .map(entry -> CalenderDtoRes.calenderMonthInquiryRes.builder()
                        .day(entry.getKey())
                        .calenderInquiryLists(entry.getValue())
                        .build())
                .collect(Collectors.toList());


        // 결과 생성
        return CalenderConverter.calenderMonthInquiryResultRes(userId,calenderMonthInquiryResList);

    }

    //글(목록) 수정
    @Transactional
    public void calenderCommentModify(Long userId, CalenderDtoReq.calenderCommentModify request){
        userRepository.findById(userId);

        Calender calender = calenderRepository.findById(request.getCalenderId());

        calender.updateComment(request.getComment());

    }
    @Transactional
    //글(목록) 줄 긋기
    public void calenderStatusModify(Long calenderId){
        Calender calender = calenderRepository.findById(calenderId);
        calender.toggleStatus();
    }
    @Transactional
    //글(목록) 삭제
    public void calenderDelete(Long calenderId){
        calenderRepository.findById(calenderId);

        calenderRepository.deleteById(calenderId);
    }

}
