package weatherwhere.team.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.schedule.ScheduleDto;
import weatherwhere.team.repository.schedule.ScheduleDtoRepository;
import weatherwhere.team.service.ScheduleService;
import weatherwhere.team.web.SessionConst;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleApiController {

    private final ScheduleDtoRepository scheduleDtoRepository;

    //dto 전용 레포지토리 생성
    //지연로딩 , 양방향 참조 문제 , N+1 문제 등 피하기
    //컨트롤러가 레포지토리를 의존하게 하기
    @GetMapping("/api/find")
    public List<ScheduleDto> scheduleTest(){
        List<ScheduleDto> events = scheduleDtoRepository.findScheduleDtos();
        return events;
    }
    //출력예
    //[
    //    {
    //        "id": 1,
    //        "title": "qweqwe",
    //        "classNames": "qwer",
    //        "start": "2023-04-19T02:40:00",
    //        "end": "2023-04-19T05:30:00",
    //        "backgroundColor": "hsl(210, 100%, 80%)",
    //        "textColor": "hsl(210, 90%, 30%)",
    //        "borderColor": "hsl(210, 100%, 80%)",
    //        "extendedProps": {
    //            "childRegion": "영도구",
    //            "description": "qweqweqe",
    //            "parentRegion": "부산광역시"
    //        }
    //    }
    //]

    @GetMapping("/api/find/member")
    public List<ScheduleDto> schedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        List<ScheduleDto> events = scheduleDtoRepository.findScheduleDtos();
        List<ScheduleDto> collect = events.stream().filter(scheduleDto -> scheduleDto.getClassNames().equals(loginMember.getUserId())).collect(Collectors.toList());
        return collect;
    }

}
