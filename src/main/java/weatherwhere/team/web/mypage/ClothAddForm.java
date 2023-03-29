package weatherwhere.team.web.mypage;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClothAddForm {
    private Long cId;

    private String userId;

    @NotBlank
    private String cName;

    private String cKind1;
    private String cKind2;
    private String cKind3;

    @NotBlank
    private String cWhereToBuy;

    private String cColor;

    private String cPhoto; //사진이름
    private String cPhotoPath; //사진경로
}
