package weatherwhere.team.web.mypage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiaryAddForm {
    private String dId;
    private String userId;
    private String dDate;
    private String dLocation1;
    private String dLocation2;
    private List<Long> dCody;
    private Integer dScore;
    private String dContent;
    private String dPhotoName;
    private String dPhotoPath;

    public DiaryAddForm() {
    }

    public DiaryAddForm(String dId, String userId, String dDate, String dLocation1, String dLocation2, List<Long> dCody, Integer dScore, String dContent, String dPhotoName, String dPhotoPath) {
        this.dId = dId;
        this.userId = userId;
        this.dDate = dDate;
        this.dLocation1 = dLocation1;
        this.dLocation2 = dLocation2;
        this.dCody = dCody;
        this.dScore = dScore;
        this.dContent = dContent;
        this.dPhotoName = dPhotoName;
        this.dPhotoPath = dPhotoPath;
    }
}
