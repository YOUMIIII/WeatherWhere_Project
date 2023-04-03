package weatherwhere.team.domain.board;

import lombok.Getter;
import lombok.Setter;
import weatherwhere.team.repository.board.CommentDTO;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "commentTable")
public class CommentEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id; //댓글번호

    @Column(length = 20, nullable = false)
    private String userId;

    @Column
    private String commentContents;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;


    //DTO를 엔티티로 타입 변경
    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUserId(commentDTO.getUserId());
        System.out.println("\uD83D\uDC9A댓글 작성한 userId = " + commentEntity.getUserId());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}