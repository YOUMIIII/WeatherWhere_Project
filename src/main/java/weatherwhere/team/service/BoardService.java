package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.board.BoardFileEntity;
import weatherwhere.team.domain.board.FavoriteEntity;
import weatherwhere.team.repository.board.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final FavoriteRepository favoriteRepository;


    public Long save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
//        if (boardDTO.getBoardFile() == null) {
            System.out.println("BoardService.save 실행됨.");

            // 첨부 파일 없음.
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            System.out.println("boardEntity = " + boardEntity);
            Long savedId = boardRepository.save(boardEntity).getId();
            return savedId;
        } else {
            // 첨부 파일 있음.
            /*
             * 1. DTO에 담긴 파일을 꺼낸다.
             * 2. 파일 이름을 가져온다.
             * 3. 서버 저장용 이름을 만든다.
             *    ex)사진.jpg -> 123456_사진.jpg
             * 4. 저장 경로 설정
             * 5. 해당 결로에 파일 저장
             * 6. boardTable에 해당 데이터 save 처리
             * 7. boardFile에 해당 데이터 save 처리
             * */

            MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
            String originalFilename = boardFile.getOriginalFilename(); // 2.
            String storedFileName = "image_" + originalFilename; // 3.

            //transferTo 설정 방법1
//            String fileSavePath = System.getProperty("user.dir") + "/src/main/resources/static/img/FileAttached"; //4
//            boardFile.transferTo(new File(fileSavePath, storedFileName)); // 5.


            //transferTo 설정 방법2
            String fileSavePath = System.getProperty("user.dir") + "/src/main/resources/static/img/FileAttached/" + storedFileName;
            boardFile.transferTo(new File(fileSavePath)); // 5.

            System.out.println("fileSavePath = " + fileSavePath);

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);

            return savedId;
        }

    }

    //DB에 저장된 게시글 목록 불러오기
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    //DB에 저장된 즐겨찾기 목록 불러오기
    @Transactional
    public List<FavoriteDTO> findAll(String memberId) {
        List<FavoriteEntity> favoriteEntityList = favoriteRepository.findAll();
        List<FavoriteDTO> favoriteDTOList = new ArrayList<>();
        for (FavoriteEntity favoriteEntity : favoriteEntityList) {
            favoriteDTOList.add(FavoriteDTO.toFavoriteDTOList(favoriteEntity));
//            favoriteDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return favoriteDTOList;
    }

//    public Long favoriteSave(FavoriteDTO favoriteDTO) throws IOException {
//        //여기에 보드디티오를 넣어야 하는가 페이보디티오를 넣어야 하는가???
//        FavoriteEntity favoriteEntity = FavoriteEntity.toSaveEntity(favoriteDTO, favoriteDTO.getMemberId() );
//
//        Long id = favoriteRepository.save(favoriteEntity).getId();
////        Long savedId = boardRepository.save(boardEntity).getId();
//        return id;
//    }

    //아래 메서드와 합쳐야함.

    public void favoriateSave(BoardDTO boardDTO, String loginId) throws IOException {
        System.out.println("\uD83D\uDC99Controller에서 넘어온 boardDTO = " + boardDTO); //넘어온 게시글 정보 
        System.out.println("\uD83D\uDC99Controller에서 넘어온 loginId = " + loginId); // 넘어온 로그인 ID


        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //게시글 정보 엔티티로 타입 변경
//        Long savedId = boardRepository.save(boardEntity).getId(); //이거 아마 게시글번호?
//        BoardEntity favoBoardEntity = boardRepository.findById(savedId).get();
//        Member memberId = //컨드롤러에서 멤버 넘겨받기

        //타입변경한 엔티티와 로그인 ID를 즐겨찾기 엔티티에 넣기
        FavoriteEntity favoEntity = FavoriteEntity.toSaveEntity(boardEntity, loginId); //
//💙
        //fileEntity도 거쳐야할까??

//        Long favoriteEntityId = favoriteEntity.getId();
        if(!favoriteRepository.findByMemberIdAndBoardEntity(loginId, Optional.of(boardEntity)).isPresent()){
            favoriteRepository.save(favoEntity); //이 엔티티 저장하기
        }

//        return favoEntity.getId();
    }

    //원본
    public Page<BoardDTO> favoritePaging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<FavoriteEntity> boardEntities =
                favoriteRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
//        Page<BoardEntity> boardEntities =
//                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부


        // 목록: id, postType, userid, title, hits, postdateTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> BoardDTO.toBoardDTO(board.getBoardEntity()));
//        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getPostType(), board.getUserId(), board.getTitle(), board.getHits(), board.getPostdateTime()));
        return boardDTOS;
    }

//   public Page<FavoriteDTO> favoritePaging(Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;
//        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
//        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
//        // page 위치에 있는 값은 0부터 시작
//
//
//        Page<FavoriteEntity> favoBoardEntities =
//                favoriteRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
////        Page<BoardEntity> boardEntities =
////                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
//
//        System.out.println("boardEntities.getContent() = " + favoBoardEntities.getContent()); // 요청 페이지에 해당하는 글
//        System.out.println("boardEntities.getTotalElements() = " + favoBoardEntities.getTotalElements()); // 전체 글갯수
//        System.out.println("boardEntities.getNumber() = " + favoBoardEntities.getNumber()); // DB로 요청한 페이지 번호
//        System.out.println("boardEntities.getTotalPages() = " + favoBoardEntities.getTotalPages()); // 전체 페이지 갯수
//        System.out.println("boardEntities.getSize() = " + favoBoardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
//        System.out.println("boardEntities.hasPrevious() = " + favoBoardEntities.hasPrevious()); // 이전 페이지 존재 여부
//        System.out.println("boardEntities.isFirst() = " + favoBoardEntities.isFirst()); // 첫 페이지 여부
//        System.out.println("boardEntities.isLast() = " + favoBoardEntities.isLast()); // 마지막 페이지 여부
//
//
//        // 목록: id, postType, userid, title, hits, postdateTime
//        Page<FavoriteDTO> faveboardDTOS = favoBoardEntities.map(board -> FavoriteDTO.toFavoriteDTO(favoriteRepository.getReferenceById(board.getId()), board.getId()));
////        Page<BoardDTO> boardDTOS = boardEntities.map(board -> BoardDTO.toBoardDTO(board.getBoardEntity()));
////        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getPostType(), board.getUserId(), board.getTitle(), board.getHits(), board.getPostdateTime()));
//        return faveboardDTOS;
//    }


    //게시글 수 확인
    public Long count() {
        Long count = boardRepository.count();
//        System.out.println("count = " + count); // 총 게시글 수 확인
        return count;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            System.out.println("\uD83D\uDC9A 글번호로 게시글 찾기");
            System.out.println("조회해서 반환할 boardDTO = " + boardDTO);
            return boardDTO;
        } else {
            return null;
        }
    }

    public FavoriteDTO favoriteFindById(Long id) {
        Optional<FavoriteEntity> byId = favoriteRepository.findById(id);

        System.out.println("byId = " + byId);
        FavoriteEntity favoriteEntity = byId.get();
        FavoriteDTO favoriteDTO = FavoriteDTO.toFavoriteDTOList(favoriteEntity);

        return favoriteDTO;

//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
//        if (optionalBoardEntity.isPresent()) {
//            BoardEntity boardEntity = optionalBoardEntity.get();
//            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
//            System.out.println("\uD83D\uDC9A 글번호로 게시글 찾기");
//            System.out.println("조회해서 반환할 boardDTO = " + boardDTO);
//            return boardDTO;
//        } else {
//            return null;
//        }
    }

    public BoardDTO update(BoardDTO boardDTO) { //위의 save와 비슷하게 수정해야할듯???
        //todo(dayi) : bController의 update메서드에서 이 메서드를 호출하여 실행할 때 이 메서드 안에 호출된 toUpdateEntity를 수정해야할거 같다.
        //BoardEntity의 toUpdateEntity 메서드 수정하기 파일 첨부가 유지되도록
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO); //여긴 한거 같은데
        boardRepository.save(boardEntity); //이제 여기를 수정할 차례, 여기까지 수정해보고 실행해본 뒤에 제대로 동작하는지 확인하기
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        deleteFavorite(id);
        boardRepository.deleteById(id);
    }


    //즐겨찾기 목록 삭제
    public void deleteFavorite(Long id) {

        Optional<BoardEntity> boardRepositoryById = boardRepository.findById(id);
        Long favId = favoriteRepository.findByBoardEntity(boardRepositoryById).get().getId();
        System.out.println("💙favId = " + favId);
        favoriteRepository.deleteById(favId);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, postType, userid, title, hits, postdateTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> BoardDTO.toBoardDTO(board));
        return boardDTOS;
    }


    @Transactional
    public Page<BoardDTO> searchRegionAndPaging(String parentRegion, String childRegion, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5;
        // 지역1, 지역2, Pageable 로 DB 에서 조건 검색
        Page<BoardEntity> boardEntityList = boardRepository.findAllBoardEntityByParentRegionAndChildRegion(parentRegion, childRegion, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        //DTO 에 담기
        Page<BoardDTO> dtoList = boardEntityList.map(boardEntity -> BoardDTO.toBoardDTO(boardEntity));
        //Page<BoardDTO> 로 반환
        return dtoList;
        //class PageImpl<T> extends Object implements Page<T>
        //생성자 : PageImpl(List<T> content)
    }


}
