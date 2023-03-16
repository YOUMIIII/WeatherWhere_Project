package weatherwhere.team.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Region;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegionRepository {

    private final EntityManager em;

    public void save(Region region){
        em.persist(region);
    }

    public Region findOne(Long id){
        return em.find(Region.class,id);
    }

    public List<Region> findAll(){
        return em.createQuery("select r from Region r", Region.class)
                .getResultList();
    }


}
