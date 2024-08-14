package Growup.spring.calender.presentation;

import Growup.spring.calender.application.CalenderService;
import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.domain.Calender;
import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/growup/calender")
public class CalenderController {

    private final CalenderService calenderService;

    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 목록 (달기준) 조회
     */
    @GetMapping("/inquiry-month")
    public ApiResponse<CalenderDtoRes.calenderMonthInquiryResultRes> calenderMonthInquiry(@AuthenticationPrincipal Long userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth day) {
        return ApiResponse.onSuccess(calenderService.calenderMonthInquiry(userId, day));
    }

    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 해당 날짜(일) 조회
     */
    @GetMapping("/inquiry")
    public ApiResponse<CalenderDtoRes.calenderInquiryResultRes> calenderInquiry(@AuthenticationPrincipal Long userId,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day){
        return ApiResponse.onSuccess(calenderService.calenderInquiry(day,userId));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 목록 등록
     */
    @PostMapping("/enroll")
    public ApiResponse<CalenderDtoRes.calenderEnrollRes> calenderEnroll(@AuthenticationPrincipal Long userId,@RequestBody @Valid CalenderDtoReq.calenderEnroll request){
        Calender calender = calenderService.calenderEnroll(userId,request);

        return ApiResponse.onSuccess(CalenderConverter.calenderEnrollRes(userId,calender));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 목록 수정(글)
     */
    @PatchMapping("/comment-modify")
    public ApiResponse<SuccessStatus> calenderCommentModify(@AuthenticationPrincipal Long userId,@RequestBody @Valid CalenderDtoReq.calenderCommentModify request){
        calenderService.calenderCommentModify(userId,request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 목록 상태 수정(줄 긋기)
     */
    @PatchMapping("/status-modify")
    public ApiResponse<SuccessStatus> calenderStatusModify(@RequestParam Long calenderId){
        calenderService.calenderStatusModify(calenderId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.14 수정자(리팩토링) : 정주현
     * 목록 삭제
     */
    @DeleteMapping("/delete")
    public ApiResponse<SuccessStatus> calenderDelete(@RequestParam Long calenderId){
        calenderService.calenderDelete(calenderId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }





}
