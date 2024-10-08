package Growup.spring.growRoom.model;


import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.component.Number;
import Growup.spring.growRoom.model.component.*;
import Growup.spring.liked.model.Liked;
import Growup.spring.participate.model.Participate;
import Growup.spring.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GrowRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Integer default 0", nullable = false, length =40)
    private Integer view;

    @Column(columnDefinition = "VARCHAR(40) DEFAULT '모집중'", nullable = false, length = 40)
    private String status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<GrowRoomCategory> growRoomCategoryList = new ArrayList<>(3);

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Participate> participateList = new ArrayList<>();

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Liked> likeList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitmentId")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numberId")
    private Number number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodId")
    private Period period;

    @OneToOne
    @JoinColumn(name = "recruitmentPeriodId")
    private RecruitmentPeriod recruitmentPeriod;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Builder    // 빌더 패턴으로 객체 생성
    public GrowRoom(Recruitment recruitment, Number number, Period period, Post post) {
        this.recruitment = recruitment;
        this.number = number;
        this.period = period;
        this.post = post;
    }

    public void update(Recruitment recruitment, Number number, Period period, List<GrowRoomCategory> growRoomCategories){
        this.recruitment = recruitment;
        this.number = number;
        this.period = period;
        this.growRoomCategoryList = growRoomCategories;
        this.updatedAt = LocalDateTime.now();
    }

    // 삭제 - status 변경
    public void updateStatus(String status){
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}