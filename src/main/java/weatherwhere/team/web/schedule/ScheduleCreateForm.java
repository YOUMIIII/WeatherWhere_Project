package weatherwhere.team.web.schedule;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ScheduleCreateForm {


    @NotEmpty
    private String date;

    @NotNull
    private String startHour;

    @NotNull
    private String startMin;

    @NotNull
    private String endHour;

    @NotNull
    private String endMin;

    @Size(min = 1, max = 16, message = "제목은 1글자 이상 16글자 이하로 입력해주세요")
    private String title;

    @Size(max = 16, message = "내용은 16글자 이하로 입력해주세요")
    private String description;

    @NotEmpty
    private String userLocation;

    @NotEmpty
    private String userLocation2;
    @NotEmpty
    private String sColor;//red,orange,...,purple
}
