package weatherwhere.team.repository.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Weather;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeatherRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Weather weather){
        em.persist(weather);
    }

    public Weather findOne(Long id){
        return em.find(Weather.class, id);
    }

    public List<Weather> findAll() {
        return em.createQuery("select w from Weather w", Weather.class)
                .getResultList();
    }

    public List<Weather> findByNxNyBaseTime(Integer nx,Integer ny,String baseDateTime){
        List<Weather> resultList = em.createQuery("select w from Weather w where w.baseDateTime=:baseDateTime and w.nx=:nx and w.ny=:ny", Weather.class)
                .setParameter("baseDateTime",baseDateTime)
                .setParameter("nx", nx)
                .setParameter("ny", ny)
                .getResultList();
        return resultList;
    }


}
