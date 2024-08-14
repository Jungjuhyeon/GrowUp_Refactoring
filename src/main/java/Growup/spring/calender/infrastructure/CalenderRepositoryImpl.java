package Growup.spring.calender.infrastructure;

import Growup.spring.calender.application.CalenderRepository;
import Growup.spring.calender.domain.Calender;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.constant.handler.CalenderHandler;
import Growup.spring.constant.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class CalenderRepositoryImpl implements CalenderRepository {
    private final CalenderJpaRepository calenderJpaRepository;

    @Override
    public Calender save(Calender calender){
        return calenderJpaRepository.save(calender);
    }

    @Override
    public List<CalenderDtoRes.calenderInquiryRes> findDayByUserId(Long userId, LocalDate day){
        return calenderJpaRepository.findDayByUserId(userId,day);
    }
    @Override
    public List<Calender> findMonthDayByUserId(LocalDate startOfMonth,LocalDate endOfMonth, Long userId){
        return calenderJpaRepository.findMonthDayByUserId(startOfMonth, endOfMonth, userId);
    }

    @Override
    public Calender findById(Long calenderId){
        return calenderJpaRepository.findById(calenderId).orElseThrow(()->new CalenderHandler(ErrorStatus.CALENDER_NOT_FOUND));
    }

    @Override
    public void deleteById(Long calenderId){
        calenderJpaRepository.deleteById(calenderId);
    }

}
