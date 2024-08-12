package Growup.spring.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public class UserDtoReq {

    @Setter
    @Getter
    public static class userRegisterReq{

        @NotBlank(message = "이름은 공백일 수 없습니다.")
        private String name;
        @Size(message = "닉네임은 2글자 이상, 10글자 이하입니다.", min= 2, max = 10)
        private String nickName;
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\\\.[a-zA-Z]{2,3}$",message = "이메일 형식이 아닙니다.")
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",message = "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다.")
        private String password;
        private String passwordCheck;


    }
    @Getter
    public static class userLoginReq{
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\\\.[a-zA-Z]{2,3}$",message = "이메일 형식이 아닙니다.")
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",message = "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class passwordRestoreReq{
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",message = "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다.")
        private String password;
        @NotBlank
        private String passwordCheck;
    }

    @Getter
    @Setter
    public static class nicknameDuplicationReq{
        @Size(message = "닉네임은 2글자 이상, 10글자 이하입니다.", min= 2, max = 10)
        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        private String nickName;

    }

    @Getter
    @Setter
    public static class currentPasswordCheckReq{
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",message = "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다.")
        private String currentPwd;
    }

    @Getter
    @Setter
    public static class withdrawReq{
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",message = "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다.")
        private String currentPwd;
    }


}
