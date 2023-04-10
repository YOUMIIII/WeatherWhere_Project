package weatherwhere.team.repository.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Schedule;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ScheduleRepository {
    @PersistenceContext
    private final EntityManager em;

    public Long save(Schedule schedule){
        em.persist(schedule);
        return schedule.getId();
    }

    public Schedule findOne(Long id){
        return em.find(Schedule.class, id);
    }

    public List<Schedule> findAllWithMember(){
        return em.createQuery("select s from Schedule s"+
                                    " join fetch s.member m",
                                    Schedule.class)
                .getResultList();
    }

    public void remove(Schedule schedule){
        em.remove(schedule);
    }

}
