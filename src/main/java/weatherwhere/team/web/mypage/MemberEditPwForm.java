package weatherwhere.team.web.mypage;

import lombok.Data;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.web.member.MemberForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberEditPwForm extends MemberForm {
    @NotNull @Size(min = 8, max = 16, message = "최소 8글자, 최대 16글자 이하로 입력해주세요")
    private String userPw;

    @NotNull(message = "필수 입력란 입니다")
    private String userPwCheck;

    public static MemberEditPwForm createMemberEditPwForm(Member member){
        MemberEditPwForm form = new MemberEditPwForm();
        form.setUserPw(member.getUserPw());
        return form;
    }
}
