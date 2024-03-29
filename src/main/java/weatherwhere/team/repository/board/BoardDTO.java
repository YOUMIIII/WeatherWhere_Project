package weatherwhere.team.repository.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.board.BoardEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardDTO {
    private Long id;
    private String userId;
    private String title;
    private String contents;
    private String postType;
    private String parentRegion;
    private String childRegion;
    private int hits;
    private LocalDateTime postdateTime;
    private LocalDateTime updateTime;

    private MultipartFile boardFile; // save.html -> Controller 파일 담는 용도
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)


    // 엔티티를 DTO로 타입 변경
    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        //게시글 수, 글 유형, 글 제목, 글 내용, 유저, 조회수, 작성일, 수정일, 첨부파일 O
        boardDTO.setId(boardEntity.getId()); // 게시글 수
        boardDTO.setPostType(boardEntity.getPostType()); // 글 유형
        boardDTO.setTitle(boardEntity.getTitle()); // 제목
        boardDTO.setUserId(boardEntity.getUserId()); // 작성자
        boardDTO.setHits(boardEntity.getHits()); // 조회수
        boardDTO.setContents(boardEntity.getContents());  // 내용
        boardDTO.setPostdateTime(boardEntity.getPostdateTime()); // 작성일
        boardDTO.setUpdateTime(boardEntity.getUpdateTime()); // 수정일
        //지역 추가
        boardDTO.setParentRegion(boardEntity.getParentRegion()); //지역
        boardDTO.setChildRegion(boardEntity.getChildRegion()); //지역

        //첨부파일 여부
        if (boardEntity.getFileAttached() == 0) { //boardEntity에 첨부파일이 없다면
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        } else if(boardEntity.getBoardFileEntityList().size()!=0) {
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
            // 파일 이름을 가져가야 함.
            // orginalFileName, storedFileName : board_file_table(BoardFileEntity)
            // join
            // select * from board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.id=?
        }

        return boardDTO;
    }
}
