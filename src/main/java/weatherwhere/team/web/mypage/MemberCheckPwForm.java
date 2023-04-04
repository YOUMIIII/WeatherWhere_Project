package weatherwhere.team.web.mypage;

import lombok.Data;
import weatherwhere.team.web.member.MemberForm;

import javax.validation.constraints.NotNull;

@Data
public class MemberCheckPwForm extends MemberForm {
    @NotNull
    private String userPw;
}
