package weatherwhere.team.web.mypage;

import lombok.Data;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.web.member.MemberForm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberEditForm extends MemberForm {
    @NotNull(message = "필수 입력란 입니다")
    @Size(min = 2, max = 10, message = "아이디는 최소 2글자, 최대 10글자 이하로 입력해주세요")
    private String userId;

    @Email(message = "이메일 형식이 올바르지 않습니다")
    @NotBlank(message = "필수 입력란 입니다")
    private String userMail;
    @NotNull
    private String userLocation;
    @NotNull
    private String userLocation2;
    private String userPhoto;

    public static MemberEditForm createMemberEditForm(Member member){
        MemberEditForm form=new MemberEditForm();
        form.setUserId(member.getUserId());
        form.setUserMail(member.getUserMail());
        form.setUserLocation(member.getParentRegion());
        form.setUserLocation2(member.getChildRegion());
        form.setUserPhoto(member.getUserPhoto());
        return form;
    }
}
