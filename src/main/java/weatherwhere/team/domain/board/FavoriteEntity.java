package weatherwhere.team.domain.board;


import lombok.Getter;
import lombok.Setter;
import weatherwhere.team.repository.board.BoardDTO;

import javax.persistence.*;


@Entity(name = "favorite")
@Getter
@Setter
@Table(name = "favorite")
public class FavoriteEntity {
    @Id //pk 컬럼 지정, 필수
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;
//    private Long favoriteId;

    @Column
    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;



    //BoardEntity -> FavoriteEntity

    public static FavoriteEntity toSaveEntity(BoardEntity boardEntity, String memberId) { //즐겨찾기 저장할때
        FavoriteEntity favoriteEntity = new FavoriteEntity();

        favoriteEntity.setId(boardEntity.getId());//글번호
//        favoriteEntity.setId(favoriteEntity.getId());//글번호
        favoriteEntity.setMemberId(memberId); //로그인 ID
        favoriteEntity.setBoardEntity(boardEntity); //게시글 정보

        return favoriteEntity;
    }

    public static FavoriteEntity toSetEntity(BoardEntity boardEntity, String memberId) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setId(boardEntity.getId());
        favoriteEntity.setMemberId(memberId);

        return favoriteEntity;
    }




}
