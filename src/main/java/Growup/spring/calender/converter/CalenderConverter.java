package Growup.spring.calender.converter;

import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.domain.Calender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalenderConverter {

    //캘린더 목록 등록 응답
    public static CalenderDtoRes.calenderEnrollRes calenderEnrollRes(Long userId, Calender calender){
        return CalenderDtoRes.calenderEnrollRes.builder()
                .userId(userId)
                .calenderId(calender.getId())
                .createAt(calender.createdAt)
                .build();
    }

    //캘린더 목록 조회
    public static CalenderDtoRes.calenderInquiryRes calenderInquiryRes(Calender calender){
        return CalenderDtoRes.calenderInquiryRes.builder()
                .calenderId(calender.getId())
                .comment(calender.getComment())
                .status(calender.getStatus())
                .build();
    }

    //캘린더 특정 날짜 조회 응답
    public static CalenderDtoRes.calenderInquiryResultRes calenderInquiryResultRes(Long userId, LocalDate day, List<CalenderDtoRes.calenderInquiryRes> calenderInquiryRes){
        return CalenderDtoRes.calenderInquiryResultRes.builder()
                .userId(userId)
                .day(day)
                .calenderInquiryLists(calenderInquiryRes)
                .build();
    }

    //캘린더 특정 달 조회 총 결과
    public static CalenderDtoRes.calenderMonthInquiryResultRes calenderMonthInquiryResultRes(Long userId,List<CalenderDtoRes.calenderMonthInquiryRes> calenderMonthInquiryResList) {
        return CalenderDtoRes.calenderMonthInquiryResultRes.builder()
                .userId(userId)
                .calenderMonthInquiryLists(calenderMonthInquiryResList)
                .build();
    }



}
