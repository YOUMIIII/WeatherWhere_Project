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
    //userId를 연동하는걸 해야할거 같은데 우선은 이렇게 따로 작동하도록

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
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}