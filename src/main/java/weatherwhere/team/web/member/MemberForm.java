package weatherwhere.team.web.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public abstract class MemberForm {
    private String userId;
    private String userMail;

    private String userLocation;

    private String userLocation2;
    private String userPhoto;
}
