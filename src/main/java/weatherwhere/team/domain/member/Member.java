package weatherwhere.team.domain.member;

import lombok.Data;

@Data
public class Member {
    private Long id;
    private String userId;
    private String userPw;

//    비번확인은 저장 필요 없을거같아서 일단 주석처리
//    private String userPwCheck;
    private String userMail;
/*    private String userLocation;
    private String userLocation2;*/
    private Long userLocationNum;
    private String userPhoto;

}
