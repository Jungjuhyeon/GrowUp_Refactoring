package Growup.spring.todoList.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


public class TodoDtoListReq {

    //todolist 등록 요청
    @Getter
    public static class todoEnrollReq{
        @NotBlank
        private String comment;
    }

    @Getter
    public static class todoCommentModifyReq{
        private String comment;
    }



}

