package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.closet.Cloth;
import weatherwhere.team.domain.closet.Diary;
import weatherwhere.team.domain.closet.DiaryContents;
import weatherwhere.team.domain.closet.DiaryInfo;
import weatherwhere.team.repository.closet.ClothRepository;
import weatherwhere.team.web.mypage.ClothUpdateForm;

import java.io.File;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClothService {
    private final ClothRepository clothRepository;

    /* 파일 넣기 전에 짠 코드
    public Cloth save(Cloth cloth, MultipartFile file) throws Exception{

        return clothRepository.save(cloth);
    }*/


    public Cloth save(Cloth cloth, MultipartFile file) throws Exception{

        if(cloth.getCName() != null && cloth.getCWhereToBuy() != null && cloth.getCKind1() != null){
            if(file.isEmpty()){
                cloth.setCPhoto("nofile");
                //파일 첨부 안할경우 기본사진으로
                cloth.setCPhotoPath("/img/files/0dc00081-7a6f-4826-927d-f3d4cbcd7100_jean.png");
            }else{
                /*우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정*/
                String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/img/files";
                log.info("projectpath = {}", projectPath);

                /*식별자 . 랜덤으로 이름 만들어줌*/
                UUID uuid = UUID.randomUUID();

                /*랜덤식별자_원래파일이름 = 저장될 파일이름 지정*/
                String fileName = uuid + "_" + file.getOriginalFilename();
                log.info("filename = {}", fileName);

                /*빈 껍데기 생성*/
                /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
                File saveFile = new File(projectPath, fileName);

                file.transferTo(saveFile);

                /*디비에 파일 넣기*/
                cloth.setCPhoto(fileName);
                /*저장되는 경로*/
                cloth.setCPhotoPath("/img/files/" + fileName); /*저장된파일의이름,저장된파일의경로. 맨 앞에 /를 붙이니까 못불러와서 뺌*/
            }
        }
        /*파일 저장*/
        return clothRepository.save(cloth);
    }

    public Cloth findById(Long id, String userId) {
        return clothRepository.findById(id, userId);
    }

    public List<Cloth> findAll(String userId){
        return clothRepository.findAll(userId);
    }

    public void update(Long cId, ClothUpdateForm updateParam, MultipartFile file) throws Exception {
        if(updateParam.getCName() != null && updateParam.getCWhereToBuy() != null && updateParam.getCKind1() != null){
            if(file.isEmpty()){
                updateParam.setCPhoto("nofile");
                //파일 첨부 안할경우 기본사진으로
                updateParam.setCPhotoPath("/img/files/0dc00081-7a6f-4826-927d-f3d4cbcd7100_jean.png");
            }else{
                /*우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정*/
                String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/img/files";
                log.info("projectpath = {}", projectPath);

                /*식별자 . 랜덤으로 이름 만들어줌*/
                UUID uuid = UUID.randomUUID();

                /*랜덤식별자_원래파일이름 = 저장될 파일이름 지정*/
                String fileName = uuid + "_" + file.getOriginalFilename();
                log.info("filename = {}", fileName);

                /*빈 껍데기 생성*/
                /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
                File saveFile = new File(projectPath, fileName);

                file.transferTo(saveFile);

                /*디비에 파일 넣기*/
                updateParam.setCPhoto(fileName);
                /*저장되는 경로*/
                updateParam.setCPhotoPath("/img/files/" + fileName); /*저장된파일의이름,저장된파일의경로. 맨 앞에 /를 붙이니까 못불러와서 뺌*/
            }
        }
        clothRepository.update(cId, updateParam);
    }

    public void delete(Long cId, String userId){
        clothRepository.delete(cId, userId);
    }

    public DiaryInfo saveDI(DiaryInfo diaryInfo){
        return clothRepository.saveDI(diaryInfo);
    }

    public DiaryContents saveDC(DiaryContents diaryContents, MultipartFile file) throws Exception{
            if(file.isEmpty()){
                diaryContents.setDPhotoName("emptyphoto");
                //파일 첨부 안할경우 기본사진으로
                diaryContents.setDPhotoPath("/img/mypage/diary/emptyphoto.png");
            }else{
                /*우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정*/
                String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/img/mypage/diary";
                log.info("projectpath = {}", projectPath);

                /*식별자 . 랜덤으로 이름 만들어줌*/
                UUID uuid = UUID.randomUUID();

                /*랜덤식별자_원래파일이름 = 저장될 파일이름 지정*/
                String fileName = uuid + "_" + diaryContents.getUserId();
                log.info("filename = {}", fileName);

                /*빈 껍데기 생성*/
                /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
                File saveFile = new File(projectPath, fileName);

                file.transferTo(saveFile);

                /*디비에 파일 넣기*/
                diaryContents.setDPhotoName(fileName);
                /*저장되는 경로*/
                diaryContents.setDPhotoPath("/img/mypage/diary/" + fileName); /*저장된파일의이름,저장된파일의경로. 맨 앞에 /를 붙이니까 못불러와서 뺌*/
            }
        return clothRepository.saveDC(diaryContents);
    }

    public List<Diary> findAllDiary(String userId){
        return clothRepository.findAllDiary(userId);
    }

}
