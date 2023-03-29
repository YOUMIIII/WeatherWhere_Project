package weatherwhere.team.web.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClothUpdateForm {
    private String userId;
    private String cName;
    private String cKind1;
    private String cKind2;
    private String cKind3;
    private String cWhereToBuy;
    private String cColor;
    private String cPhoto;
    private String cPhotoPath;

    public ClothUpdateForm() {
    }

    public ClothUpdateForm(String userId, String cName, String cKind1, String cKind2, String cKind3, String cWhereToBuy, String cColor, String cPhoto, String cPhotoPath) {
        this.userId = userId;
        this.cName = cName;
        this.cKind1 = cKind1;
        this.cKind2 = cKind2;
        this.cKind3 = cKind3;
        this.cWhereToBuy = cWhereToBuy;
        this.cColor = cColor;
        this.cPhoto = cPhoto;
        this.cPhotoPath = cPhotoPath;
    }
}
