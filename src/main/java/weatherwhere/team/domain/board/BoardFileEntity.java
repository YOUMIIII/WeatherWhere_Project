package weatherwhere.team.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "boardFile")
public class BoardFileEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

//    @Column
//    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)// LAZY 지연로딩, EAGER 즉시로딩,  FetchType 기본은 EAGER
    @JoinColumn(name = "boardId") // 외래키
    private BoardEntity boardEntity;

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }

}

