package weatherwhere.team.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.board.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // select * from comment_table where board_id=? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}