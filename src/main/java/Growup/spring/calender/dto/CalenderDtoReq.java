package Growup.spring.calender.dto;

import Growup.spring.calender.domain.Enum.CalenderColorStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;


import java.time.LocalDate;

public class CalenderDtoReq {

    @Getter
    public static class calenderEnroll{
        @NotBlank
        private String comment;
        @NotNull
        private LocalDate day;
    }

    @Getter
    public static class calenderCommentModify{
        @NotNull
        private Long calenderId;
        @NotBlank
        private String comment;
    }

    @Getter
    public static class calenderColorModify{
        @NotNull
        private LocalDate day;
        @NotNull
        private CalenderColorStatus color;
    }




}
