package weatherwhere.team.repository.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleDtoRepository {

    private final EntityManager em;

    public List<ScheduleDto> findScheduleDtos(){
        return em.createQuery("select new weatherwhere.team.repository.schedule.ScheduleDto(s.id,m.userId,s.title,s.startTime,s.endTime,s.color,s.parentRegion,s.childRegion,s.description)"
            +" from Schedule s"
            +" join s.member m",ScheduleDto.class)
            .getResultList();
    }

}
