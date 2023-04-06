package weatherwhere.team.domain.board;


import lombok.Getter;
import lombok.Setter;
import weatherwhere.team.repository.board.BoardDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "boardTable")
public class BoardEntity extends TimeEntity {
    @Column(length = 20) // 크기 20, not null
    private String userId;

    @Id //pk 컬럼 지정, 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    @Column(length = 20)
    private String postType;

    @Column//(length = 200, nullable = false)
    private String title;

    @Column(length = 500) //, nullable = false
    private String contents;

    @Column
    private String parentRegion;

    @Column
    private String childRegion;

    @Column
    private int hits;

    @Column
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();
    //부모 엔티티 삭제
    //CascadeType.REMOVE와 orphanRemoval = true는 부모 엔티티를 삭제하면 자식 엔티티도 삭제한다.

    //부모 엔티티에서 자식 엔티티 제거
    //CascadeType.REMOVE는 자식 엔티티가 그대로 남아있는 반면, orphanRemoval = true는 자식 엔티티를 제거한다.


    //DTO를 엔티티로 타입변경
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) { //처음 저장할 때 첨부파일 X
        BoardEntity boardEntity = new BoardEntity();

        //게시글 수, 글 유형, 글 제목, 유저, 조회수, 글 내용, 첨부파일 X
        // 작성일, 수정일은 extends 하고 있으므로 안적은건가?
        boardEntity.setId(boardDTO.getId());
        boardEntity.setPostType(boardDTO.getPostType());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setUserId(boardDTO.getUserId());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setHits(-1); //조회수 시작은 0
        boardEntity.setFileAttached(0); // 파일 없음.
        //지역 추가
        boardEntity.setParentRegion(boardDTO.getParentRegion());
        boardEntity.setChildRegion(boardDTO.getChildRegion());

        System.out.println("BoardEntity.toSaveEntoity 실행됨.");
        System.out.println("boardEntity.setId = " + boardEntity.getId());
        System.out.println("boardEntity.getUserId() = " + boardEntity.getUserId());
        System.out.println("boardEntity.getTitle() = " + boardEntity.getTitle());

        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) { //수정할 때
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setPostType(boardDTO.getPostType());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setUserId(boardDTO.getUserId());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setHits(boardDTO.getHits()); // 시작이 아니므로 조회수 가져와야함.
        //todo(dayi) : 글 수정할 때 첨부파일 유지되도록 수정하기 위해 추가해야하는 부분 아직 작성 중
        boardEntity.setFileAttached(boardDTO.getFileAttached());
        //지역 추가
        boardEntity.setParentRegion(boardDTO.getParentRegion());
        boardEntity.setChildRegion(boardDTO.getChildRegion());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) { // 처음 저장할 때 첨부파일 O
        BoardEntity boardEntity = new BoardEntity();
        //게시글 수, 글 유형, 글 제목, 글 내용, 유저, 조회수, 첨부파일 O
        boardEntity.setId(boardDTO.getId());
        boardEntity.setPostType(boardDTO.getPostType());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setUserId(boardDTO.getUserId());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setHits(0); //조회수 시작은 0
        boardEntity.setFileAttached(1); // 파일 있음.
        //지역 추가
        boardEntity.setParentRegion(boardDTO.getParentRegion());
        boardEntity.setChildRegion(boardDTO.getChildRegion());
        return boardEntity;
    }
}
