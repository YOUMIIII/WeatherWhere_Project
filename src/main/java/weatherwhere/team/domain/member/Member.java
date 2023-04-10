package weatherwhere.team.domain.member;

import lombok.Getter;
import lombok.Setter;
import weatherwhere.team.domain.Schedule;
import weatherwhere.team.web.mypage.MemberEditForm;
import weatherwhere.team.web.mypage.MemberEditPwForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {
    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPw;

//    비번확인은 저장 필요 없을거같아서 일단 주석처리
//    private String userPwCheck;
    @Column(name = "user_mail")
    private String userMail;

    private String parentRegion;
    private String childRegion;

//    @Column(name = "user_location_num")
//    private Long userLocationNum;

    @Column(name = "user_photo")
    private String userPhoto;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)//연관관계 주인 : Schedule , mappedBy="Schedule 클래스에서 Member 클래스의 변수명", OneToMany 는 기본적으로 지연로딩->cascade 설정으로 인해 바로 조회됨
    private List<Schedule> scheduleList = new ArrayList<>();;

    /**
     * 멤버 객체를 생성합니다
     * Id - 자동생성 전략
     */
    public static Member createMember(String userId, String userPw, String userMail, /*Long userLocationNum,*/String parentRegion,String childRegion, String userPhoto) {
        Member member = new Member();
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setUserMail(userMail);
        member.setParentRegion(parentRegion);
        member.setChildRegion(childRegion);
        member.setUserPhoto(userPhoto);
        return member;
    }

    public static Member setEditInfo(Member member,MemberEditForm form){
        member.setUserMail(form.getUserMail());
        member.setParentRegion(form.getUserLocation());
        member.setChildRegion(form.getUserLocation2());
        member.setUserPhoto(form.getUserPhoto());
        return member;
    }

    public static Member setEditPw(Member member, MemberEditPwForm form){
        member.setUserPw(form.getUserPw());
        return member;
    }
}
