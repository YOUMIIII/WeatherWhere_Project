package weatherwhere.team.repository.closet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.closet.Cloth;
import weatherwhere.team.web.mypage.ClothUpdateForm;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ClothRepository {
    private final ClothMapper clothMapper;

    public Cloth save(Cloth cloth){
        log.info("clothmapper문제?");
        clothMapper.save(cloth);
        log.info("clothmapper문제없음");
        return cloth;
    }

    public Cloth findById(Long id, String userId) {
        return clothMapper.findById(id, userId);
    }

    public List<Cloth> findAll(String userId){
        return clothMapper.findAll(userId);
    }

    public void update(Long cId, ClothUpdateForm updateParam) {
        clothMapper.update(cId, updateParam);
    }

    public void delete(Long cId, String userId){
        clothMapper.delete(cId, userId);
    }

}
