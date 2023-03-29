package weatherwhere.team.domain.closet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cloth {
    private Long cId;
    private String userId;
    private String cName;
    private String cKind1;
    private String cKind2;
    private String cKind3;

    private String cWhereToBuy;
    private String cColor;

    private String cPhoto; //사진이름
    private String cPhotoPath; //사진경로

    public Cloth() {
    }

    public Cloth(Long cId, String userId, String cName, String cKind1, String cKind2, String cKind3, String cWhereToBuy, String cColor, String cPhoto, String cPhotoPath) {
        this.cId = cId;
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
