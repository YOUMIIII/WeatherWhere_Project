package weatherwhere.team.domain.board;


import lombok.Getter;
import lombok.Setter;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.board.BoardDTO;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "favorite")
public class FavoriteEntity {
    @Id //pk 컬럼 지정, 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    private String memberId;

    /*
      @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
      private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();
  */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;

    //DTO -> Entity

    public static FavoriteEntity save(BoardEntity boardEntity, BoardDTO boardDTO, String memberId) { //즐겨찾기 저장할때
        FavoriteEntity favoriteEntity = new FavoriteEntity();

        favoriteEntity.setId(boardDTO.getId());//글번호
        favoriteEntity.setMemberId(memberId); //로그인 ID
//        favoriteEntity.setPostdate();
//        favoriteEntity.setUserId(boardDTO.getUserId());
//        favoriteEntity.setPostType(boardDTO.getPostType());
//        favoriteEntity.setTitle(boardDTO.getTitle());
//        favoriteEntity.setContents(boardDTO.getContents());
//        favoriteEntity.setHits(boardDTO.getHits());
//        favoriteEntity.setRegion();
        favoriteEntity.setBoardEntity(boardEntity);

        return favoriteEntity;
    }






}
