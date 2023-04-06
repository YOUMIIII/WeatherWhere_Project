package weatherwhere.team.repository.region;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Region;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RegionRepository {

    @PersistenceContext
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

    public List<String> findDistinctParent(){
        return em.createQuery("select distinct r.parentRegion from Region r",String.class)
                .getResultList();
    }

    public List<Region> findByParent(String parentRegion){
        return em.createQuery("select r from Region r where r.parentRegion = :parent_region",Region.class)
                .setParameter("parent_region",parentRegion)
                .getResultList();
    }

    /**
     * parent region(시,도) child region(시,구,동)을 입력하면 지역 ID 를 가져옵니다.
     * 회원가입 폼, 회원 수정 폼, 스케줄 생성 폼 등에서 사용
     * 입력 폼은 현재 하드코딩된 상태 
     * @param parentRegion
     * @param childRegion
     * @return
     */
    public Long findByParentChild(String parentRegion, String childRegion){
        List<Region> resultList = em.createQuery("select r from Region r where r.parentRegion = :parentRegion and r.childRegion = :childRegion", Region.class)
                .setParameter("parentRegion", parentRegion)
                .setParameter("childRegion", childRegion)
                .getResultList();
        log.info("resultList 사이즈 : {}",resultList.size());
        return resultList.isEmpty()? null: resultList.get(0).getId();
    }

    public Region findByNamesAndCo(String parentRegion,String childRegion,Integer nx,Integer ny){
        List<Region> regionList = em.createQuery("select r from Region r where r.parentRegion=:parentRegion and r.childRegion=:childRegion and r.nx=:nx and r.ny=:ny", Region.class)
                .setParameter("parentRegion", parentRegion)
                .setParameter("childRegion", childRegion)
                .setParameter("nx", nx)
                .setParameter("ny", ny)
                .getResultList();
        return regionList.isEmpty()? null: regionList.get(0);
    }

}
