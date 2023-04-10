package weatherwhere.team.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.schedule.ScheduleDto;
import weatherwhere.team.web.schedule.ScheduleCreateForm;
import weatherwhere.team.web.schedule.ScheduleEditForm;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String startTime;

    private String endTime;

    private String color;//backgroundColor,textColor,borderColor

    /* extendedProps: */
    private String parentRegion;
    private String childRegion;
    private String description;
//    private Boolean completed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")//매핑을 무엇으로 할지(ERD에서 Schedule 테이블의 변수명이 됨) //foreign key name
    private Member member;

    public static Schedule dtoToSchedule(ScheduleDto scheduleDto){
        Schedule schedule=new Schedule();
        schedule.setId(scheduleDto.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        schedule.setStartTime(scheduleDto.getStart().format(formatter));
        schedule.setEndTime(scheduleDto.getEnd().format(formatter));
        schedule.setColor(scheduleDto.getBackgroundColor());
        schedule.setParentRegion((String)scheduleDto.getExtendedProps().get("parentRegion"));
        schedule.setChildRegion((String)scheduleDto.getExtendedProps().get("childRegion"));
//        schedule.setCompleted((Boolean)scheduleDto.getExtendedProps().get("completed"));
        return schedule;
    }

    public static Schedule createSchedule(ScheduleCreateForm form,Member member){
        Schedule schedule=new Schedule();
        schedule.setTitle(form.getTitle());
        StringBuilder sb = new StringBuilder();
        sb.append(form.getDate());
        sb.append(" ");
        sb.append(form.getStartHour());
        sb.append(":");
        sb.append(form.getStartMin());
        String startDateTime=sb.toString();
        schedule.setStartTime(startDateTime);
        sb = new StringBuilder();
        sb.append(form.getDate());
        sb.append(" ");
        sb.append(form.getEndHour());
        sb.append(":");
        sb.append(form.getEndMin());
        String endDateTime=sb.toString();
        schedule.setEndTime(endDateTime);
        log.info("스케줄 시작 :  {}",startDateTime);
        log.info("스케줄 종료 :  {}",endDateTime);
        schedule.setParentRegion(form.getUserLocation());
        schedule.setChildRegion(form.getUserLocation2());
        schedule.setColor(form.getSColor());
        schedule.setDescription(form.getDescription());
        schedule.setMember(member);
        return schedule;
    }
    public static void editSchedule(Schedule schedule,ScheduleEditForm form){
        schedule.setTitle(form.getTitle());
        StringBuilder sb = new StringBuilder();
        sb.append(form.getDate());
        sb.append(" ");
        sb.append(form.getStartHour());
        sb.append(":");
        sb.append(form.getStartMin());
        String startDateTime=sb.toString();
        sb = new StringBuilder();
        schedule.setStartTime(startDateTime);
        sb.append(form.getDate());
        sb.append(" ");
        sb.append(form.getEndHour());
        sb.append(":");
        sb.append(form.getEndMin());
        String endDateTime=sb.toString();
        schedule.setEndTime(endDateTime);
        log.info("스케줄 시작 :  {}",startDateTime);
        log.info("스케줄 종료 :  {}",endDateTime);
        schedule.setParentRegion(form.getUserLocation());
        schedule.setChildRegion(form.getUserLocation2());
        schedule.setColor(form.getSColor());
    }
}
