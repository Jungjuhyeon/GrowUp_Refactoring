package Growup.spring.calender.infrastructure;

import Growup.spring.calender.domain.Calender;
import Growup.spring.calender.dto.CalenderDtoRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalenderJpaRepository extends JpaRepository<Calender,Long> {
    Optional<Calender> findById(Long CalenderId);

    @Query("select new Growup.spring.calender.dto.CalenderDtoRes.calenderInquiryRes(c.Id,c.comment,c.status) from Calender c where c.userId =: userId and c.day =: day")
    List<CalenderDtoRes.calenderInquiryRes> findDayByUserId(@Param("user_id") Long userId, @Param("day")LocalDate day);

    @Query("SELECT c FROM Calender c WHERE c.userId =: userId and c.day BETWEEN :start_day AND :end_day")
    List<Calender> findMonthDayByUserId(@Param("start_day") LocalDate start_day,@Param("end_day") LocalDate end_day,@Param("user_id") Long userId);




}
