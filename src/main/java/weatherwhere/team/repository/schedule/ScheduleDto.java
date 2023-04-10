package weatherwhere.team.repository.schedule;

import lombok.Data;
import weatherwhere.team.domain.Schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScheduleDto {

    private Long id;
    private String title;
    private String classNames;//Member userId
    private LocalDateTime start;
    private LocalDateTime end;

    /* red h : 0
    , orange h : 30
    , yellow h : 60
    , green h : 120
    , sky blue h : 210
    , purple h : 270 */
    private String backgroundColor;//s,l - 100 , 80
    private String textColor;//s,l - 90 , 30
    private String borderColor;//s,l - 100 , 80

    private Map<String,Object> extendedProps;//parentRegion,childRegion,description,completed

    public ScheduleDto(Long id,String userId,String title,String start,String end,String color,String parentRegion,String childRegion,String description/*,Boolean completed*/){
        this.id=id;
        this.title=title;
        this.classNames=userId;
        this.start=LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.end=LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        //'hsl(xxx, 100%, 80%)'
        switch(color){
            case "red":
                color="hsl(0, 100%, 80%)";
                break;
            case "orange":
                color="hsl(30, 100%, 80%)";
                break;
            case "yellow":
                color="hsl(60, 100%, 80%)";
                break;
            case "green":
                color="hsl(120, 100%, 80%)";
                break;
            case "sky blue":
                color="hsl(210, 100%, 80%)";
                break;
            case "purple":
                color="hsl(270, 100%, 80%)";
                break;
        }
        this.backgroundColor=color;
        this.borderColor=color;
        this.textColor=color.substring(0,color.indexOf(",")).concat(", 90%, 30%)");//첫 , 인덱스 전까지 자르고 +", 90%, 30%)"
        this.extendedProps=new HashMap<>();
        this.extendedProps.put("parentRegion", parentRegion);
        this.extendedProps.put("childRegion", childRegion);
        this.extendedProps.put("description", description);
//        this.extendedProps.put("completed", completed);
    }


}
