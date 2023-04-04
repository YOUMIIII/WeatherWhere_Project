package weatherwhere.team.domain.board;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import weatherwhere.team.repository.board.BoardDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "favorite")
public class FavoriteEntity extends TimeEntity {
    @Id //pk 컬럼 지정, 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    @Column
    private String loginId;

    /*  @Column
      private String userId;

      @Column
      private String postType;

      @Column
      private String title;

      @Column(length = 500)
      private String contents;

      @Column
      private String region;

      @Column
      private int hits;

      @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
      private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();
  */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;

    //DTO -> Entity

    public static FavoriteEntity save(BoardEntity boardEntity, BoardDTO boardDTO, String loginId) { //즐겨찾기 저장할때
        FavoriteEntity favoriteEntity = new FavoriteEntity();

        favoriteEntity.setId(boardDTO.getId());
        favoriteEntity.setLoginId(loginId); //로그인 ID
//        favoriteEntity.setUserId(boardDTO.getUserId());
//        favoriteEntity.setPostType(boardDTO.getPostType());
//        favoriteEntity.setTitle(boardDTO.getTitle());
//        favoriteEntity.setContents(boardDTO.getContents());
//        favoriteEntity.setHits(boardDTO.getHits());
//        favoriteEntity.setRegion(boardDTO.getRegion());
        favoriteEntity.setBoardEntity(boardEntity);

        return favoriteEntity;
    }






}
