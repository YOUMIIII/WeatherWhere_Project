package weatherwhere.team.repository.closet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.closet.Cloth;
import weatherwhere.team.domain.closet.Diary;
import weatherwhere.team.domain.closet.DiaryContents;
import weatherwhere.team.domain.closet.DiaryInfo;
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
    public List<Cloth> findFavorites(String userId){
        return clothMapper.findFavorites(userId);
    }
    public List<Cloth> findTops(String userId){
        return clothMapper.findTops(userId);
    }

    public List<Cloth> findBottoms(String userId){
        return clothMapper.findBottoms(userId);
    }

    public void update(Long cId, ClothUpdateForm updateParam) {
        clothMapper.update(cId, updateParam);
    }

    public void delete(Long cId, String userId){
        clothMapper.delete(cId, userId);
    }

    public DiaryInfo saveDI(DiaryInfo diaryInfo){
        clothMapper.saveDI(diaryInfo);
        return diaryInfo;
    }

    public DiaryContents saveDC(DiaryContents diaryContents){
        clothMapper.saveDC(diaryContents);
        return diaryContents;
    }

    public void updateClothCount(Long cId){
        clothMapper.updateClothCount(cId);
    }

    public List<Diary> findAllDiary(String userId){
        return clothMapper.findAllDiary(userId);
    }


}
