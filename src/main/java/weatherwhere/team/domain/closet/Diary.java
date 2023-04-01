package weatherwhere.team.domain.closet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diary {
    //di.d_id, di.d_date, di.D_LOCATION1, di.D_LOCATION2, dc.USER_ID, dc.D_SCORE, dc.D_CONTENT,
    // dc.D_PHOTO_PATH, ci.C_NAME AS d_cody_name1, ci2.C_NAME AS d_cody_name2, ci3.c_name AS d_cody_name3
    private String dId;
    private String userId;
    private String dDate;
    private String dLocation1;
    private String dLocation2;
    private String dCodyName1;
    private String dCodyName2;
    private String dCodyName3;
    private Integer dScore;
    private String dContent;
    private String dPhotoPath;

    public Diary() {
    }

    public Diary(String dId, String userId, String dDate, String dLocation1, String dLocation2, String dCodyName1, String dCodyName2, String dCodyName3, Integer dScore, String dContent, String dPhotoPath) {
        this.dId = dId;
        this.userId = userId;
        this.dDate = dDate;
        this.dLocation1 = dLocation1;
        this.dLocation2 = dLocation2;
        this.dCodyName1 = dCodyName1;
        this.dCodyName2 = dCodyName2;
        this.dCodyName3 = dCodyName3;
        this.dScore = dScore;
        this.dContent = dContent;
        this.dPhotoPath = dPhotoPath;
    }
}
