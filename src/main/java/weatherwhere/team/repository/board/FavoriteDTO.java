package weatherwhere.team.repository.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.board.FavoriteEntity;

import javax.persistence.*;

@Getter
@Setter
@ToString
public class FavoriteDTO {

    private Long id;
    private String memberId;
    private Long boardId;
    private BoardDTO boardDTO;

//    Entity -> DTO
    public static FavoriteDTO toFavoriteDTO(FavoriteEntity favoriteEntity, Long boardId) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(favoriteEntity.getId());
        favoriteDTO.setMemberId(favoriteEntity.getMemberId());
        favoriteDTO.setBoardId(boardId);
//        favoriteDTO.setBoardId(favoriteEntity.getBoardEntity().getId());
//        favoriteDTO.setBoardDTO();
//        favoriteDTO.setBoardEntity();

        return favoriteDTO;
    }

    public static FavoriteDTO toSetFavoriteDTO(FavoriteEntity favoriteEntity) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(favoriteEntity.getId());
        favoriteDTO.setMemberId(favoriteEntity.getMemberId());

        return favoriteDTO;
    }

    public static FavoriteDTO toFavoriteDTOList(FavoriteEntity favoriteEntity) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(favoriteEntity.getId());
        favoriteDTO.setMemberId(favoriteEntity.getMemberId());
        favoriteDTO.setBoardId(favoriteEntity.getBoardEntity().getId());
//        favoriteDTO.setBoardId(favoriteEntity.getBoardEntity().getId());
//        favoriteDTO.setBoardDTO();
//        favoriteDTO.setBoardEntity();

        return favoriteDTO;
    }


}
