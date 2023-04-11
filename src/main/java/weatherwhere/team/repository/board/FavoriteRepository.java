package weatherwhere.team.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.board.FavoriteEntity;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findByBoardEntity(Optional<BoardEntity> boardEntity);

    Optional<FavoriteEntity> findByMemberIdAndBoardEntity(String memberId,Optional<BoardEntity> boardEntity);


}

//    @Query("select u from User u where u.username = :name")
//    List<User> methodName(@Param("name") String username);

//    @Query("select m from Member m where m.username = :username and m.age = :age")
//    List<Member> findUser(@Param("username") String username, @Param("age") int age);
//
//    // 단순 값 하나를 조회
//    @Query("select m.username from Member m")
//    List<String> findUsernameList();
//
//    // DTO로 직접 조회
//    @Query("select new me.kyeongho.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
//    List<MemberDto> findMemberDto();
