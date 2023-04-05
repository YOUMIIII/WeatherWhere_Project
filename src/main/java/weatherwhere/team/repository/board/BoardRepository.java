package weatherwhere.team.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import weatherwhere.team.domain.board.BoardEntity;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // update board_table set board_hits=board_hits+1 where id=?
    @Modifying
    @Query(value = "update BoardEntity b set b.hits=b.hits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);

    @Query(
            value="SELECT b FROM BoardEntity b WHERE b.parentRegion =:parentRegion and b.childRegion = :childRegion",
            countQuery = "select count(b) FROM BoardEntity b WHERE b.parentRegion =:parentRegion and b.childRegion = :childRegion"
    )
    Page<BoardEntity> findAllBoardEntityByParentRegionAndChildRegion(
            @Param("parentRegion") String parentRegion,
            @Param("childRegion") String childRegion, Pageable pageable);
}