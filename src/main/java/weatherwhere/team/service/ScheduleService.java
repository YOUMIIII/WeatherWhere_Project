package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.Schedule;
import weatherwhere.team.repository.schedule.ScheduleDto;
import weatherwhere.team.repository.schedule.ScheduleRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Long saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findScheduleByMember(){
        List<Schedule> list = scheduleRepository.findAllWithMember();
        return list;
    }

    public Schedule findSchedule(Long id){
        return scheduleRepository.findOne(id);
    }

    public void deleteSchedule(Long id){
        Schedule schedule = scheduleRepository.findOne(id);
        scheduleRepository.remove(schedule);
    }

}
