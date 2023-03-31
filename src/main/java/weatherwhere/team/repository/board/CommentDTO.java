package weatherwhere.team.repository.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import weatherwhere.team.domain.board.CommentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String userId;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentPostdate;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setUserId(commentEntity.getUserId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentPostdate(commentEntity.getPostdateTime());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
}