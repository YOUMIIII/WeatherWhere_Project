package weatherwhere.team.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import weatherwhere.team.domain.board.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {
}