package weatherwhere.team.repository.closet;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import weatherwhere.team.domain.closet.Cloth;
import weatherwhere.team.domain.closet.Diary;
import weatherwhere.team.domain.closet.DiaryContents;
import weatherwhere.team.domain.closet.DiaryInfo;
import weatherwhere.team.web.mypage.ClothUpdateForm;

import java.util.List;

@Mapper
public interface ClothMapper {
    void save(Cloth cloth);

    //파라미터가 두개 넘어갈 경우 @Param을 써준다. xml문서에서 키값같은 느낌.
    Cloth findById(@Param("cId") Long id, @Param("userId") String userId);

    List<Cloth> findAll(String userId);

    void update(@Param("cId") Long id, @Param("updateParam") ClothUpdateForm updateParam);

    void delete(@Param("cId") Long id, @Param("userId") String userId);

    void saveDI(DiaryInfo diaryInfo);

    void saveDC(DiaryContents diaryContents);

    List<Diary> findAllDiary(String userId);

}
