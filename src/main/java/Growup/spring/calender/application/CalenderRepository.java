package Growup.spring.calender.application;

import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.domain.Calender;

import java.time.LocalDate;
import java.util.List;

public interface CalenderRepository {

    public Calender save(Calender calender);

    public List<CalenderDtoRes.calenderInquiryRes> findDayByUserId(Long userId, LocalDate day);

//    public CalenderDtoRes.calenderInquiryResultRes calenderInquiry(LocalDate day, Long userId);

    public List<Calender> findMonthDayByUserId(LocalDate startOfMonth,LocalDate endOfMonth, Long userId);

    public Calender findById(Long calenderId);

    public void deleteById(Long calenderId);
}
