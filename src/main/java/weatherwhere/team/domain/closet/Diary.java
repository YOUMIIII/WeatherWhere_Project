package weatherwhere.team.domain.closet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diary {
    private String dId;
    private String userId;
    private String dDate;
    private String dLocation1;
    private String dLocation2;
    private Long dCody1;
    private Long dCody2;
    private Long dCody3;
    private Integer dScore;
    private String dContent;
    private String dPhotoName;
    private String dPhotoPath;

    public Diary() {
    }

    public Diary(String dId, String userId, String dDate, String dLocation1, String dLocation2, Long dCody1, Long dCody2, Long dCody3, Integer dScore, String dContent, String dPhotoName, String dPhotoPath) {
        this.dId = dId;
        this.userId = userId;
        this.dDate = dDate;
        this.dLocation1 = dLocation1;
        this.dLocation2 = dLocation2;
        this.dCody1 = dCody1;
        this.dCody2 = dCody2;
        this.dCody3 = dCody3;
        this.dScore = dScore;
        this.dContent = dContent;
        this.dPhotoName = dPhotoName;
        this.dPhotoPath = dPhotoPath;
    }
}
