package weatherwhere.team.web.schedule;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import weatherwhere.team.domain.Schedule;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Slf4j
public class ScheduleEditForm {

    @NotNull
    private Long id;//schedule id
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

    @NotEmpty @Size(min = 1, max = 16, message = "제목은 1글자 이상 16글자 이하로 입력해주세요")
    private String title;

    @Size(max = 16, message = "내용은 16글자 이하로 입력해주세요")
    private String description;

    @NotEmpty
    private String userLocation;
    @NotEmpty
    private String userLocation2;
    @NotEmpty
    private String sColor;//red,orange,...,purple

    public static ScheduleEditForm createScheduleEditForm(Schedule schedule){
        ScheduleEditForm form=new ScheduleEditForm();
        form.setId(schedule.getId());
        form.setDate(schedule.getStartTime().substring(0,10));
        form.setStartHour(schedule.getStartTime().substring(11,13));
        form.setStartMin(schedule.getStartTime().substring(14,16));
        form.setEndHour(schedule.getEndTime().substring(11,13));
        form.setEndMin(schedule.getEndTime().substring(14,16));
        log.info("스케줄 날짜 : {}", form.getDate());
        log.info("스케줄 시작 :  {},{}",form.getStartHour(),form.getStartMin());
        log.info("스케줄 종료 :  {},{}",form.getEndHour(),form.getEndMin());
        form.setTitle(schedule.getTitle());
        form.setDescription(schedule.getDescription());
        form.setUserLocation(schedule.getParentRegion());
        form.setUserLocation2(schedule.getChildRegion());
        form.setSColor(schedule.getColor());
        return form;
    }
}
