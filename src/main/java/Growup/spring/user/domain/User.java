package Growup.spring.user.domain;

import Growup.spring.calender.domain.Calender;
import Growup.spring.todoList.domain.TodoList;
import Growup.spring.user.domain.Enum.UserRole;
import Growup.spring.user.domain.Enum.UserState;
import Growup.spring.constant.entity.BaseEntity;

import Growup.spring.user.dto.UserDtoReq;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(length = 200)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'NONACTIVE'")
    public UserState status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    public UserRole role;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Participate> participateList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<GrowRoom> growRoomList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Pin> pinList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Liked> likeList = new ArrayList<>();
//
    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL)
    private List<TodoList> todoLists = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<Calender> calenderList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<CalenderColor> calenderColorList = new ArrayList<>();

//    @PreUpdate
//    public void preUpdate() {
//        // WITHDRAW 상태에서만 실행
//        if (UserState.WITHDRAW.equals(this.status)) {
//            this.createdAt = null; // 또는 다른 원하는 값으로 설정 가능
//            this.updatedAt = null;
//            this.setNickName(null);
//            this.setName("탈퇴계정");
//            this.setPassword("");
//            this.setNickName("");
//        }
//    }

    //== 생성 메소드 ==//
    public static User create(UserDtoReq.userRegisterReq request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .build();
    }

    //== 인증번호(완료시) 활성화 메소드==//
    public void changeStatus(){
        this.status = UserState.ACTIVE;
    }
    //==인증 확인==//
    public boolean isActive(){
        return this.status != UserState.NONACTIVE;
    }
    //==비밀번호 병경==//
    public void changePassword(String password){
        this.password = password;
    }
    //==이메일 변경==//
    public void changeEmail(String eamil){
        this.email = eamil;
    }
    //== 회원탈퇴 상태 변경==//
    public void withdraw(){
        this.status = UserState.WITHDRAW;
    }

    //== 프로필 변경 ==//
    public void changeProfile(String url){
        this.photoUrl =url;
    }
    public void changeNickname(String nickName){
        this.nickName =nickName;
    }

}