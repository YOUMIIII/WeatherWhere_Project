package weatherwhere.team.domain.closet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryContents {
    private String dId; //일기번호
    private String userId; //작성자
    private Integer dScore; //코디점수
    private String dContent; //일기내용
    private String dPhotoName; //사진이름
    private String dPhotoPath; //사진경로

    public DiaryContents() {
    }

    public DiaryContents(String dId, String userId, Integer dScore, String dContent, String dPhotoName, String dPhotoPath) {
        this.dId = dId;
        this.userId = userId;
        this.dScore = dScore;
        this.dContent = dContent;
        this.dPhotoName = dPhotoName;
        this.dPhotoPath = dPhotoPath;
    }
}
