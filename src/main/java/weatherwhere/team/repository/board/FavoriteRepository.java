package weatherwhere.team.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import weatherwhere.team.domain.board.FavoriteEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

}
