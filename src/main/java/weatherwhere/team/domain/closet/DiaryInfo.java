package weatherwhere.team.domain.closet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryInfo {
    private String dId; //일기번호
    private String dDate; //날짜
    private String dLocation1; //장소1
    private String dLocation2; //장소2
    private Long dCody1; //코디1
    private Long dCody2; //코디2
    private Long dCody3; //코디3

    public DiaryInfo() {
    }

    public DiaryInfo(String dId, String dDate, String dLocation1, String dLocation2, Long dCody1, Long dCody2, Long dCody3) {
        this.dId = dId;
        this.dDate = dDate;
        this.dLocation1 = dLocation1;
        this.dLocation2 = dLocation2;
        this.dCody1 = dCody1;
        this.dCody2 = dCody2;
        this.dCody3 = dCody3;
    }
}
