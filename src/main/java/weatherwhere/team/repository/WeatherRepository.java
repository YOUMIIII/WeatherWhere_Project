package weatherwhere.team.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Weather;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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


}
