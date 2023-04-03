package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.board.CommentEntity;
import weatherwhere.team.repository.board.BoardRepository;
import weatherwhere.team.repository.board.CommentDTO;
import weatherwhere.team.repository.board.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회 */

        System.out.println("\uD83D\uDC9A html에서 넘어온 commentDTO = " + commentDTO);
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        System.out.println("optionalBoardEntity = " + optionalBoardEntity);
        System.out.println("\uD83D\uDC9AcommentDTO.getUserId = " + commentDTO.getUserId());
        System.out.println("commentDTO.getBoardId = " + commentDTO.getBoardId());

        if (optionalBoardEntity.isPresent()) {
            
            BoardEntity boardEntity = optionalBoardEntity.get();
//            boardEntity.setUserId(commentDTO.getUserId());//내가 추가한 부분
            System.out.println("\uD83D\uDC9A 저장하기 위한 엔티티인데 boardEntity = " + boardEntity);
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        System.out.println("게시글 번호 확인 = " + boardEntity.getId()); // 게시글번호 확인

        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            System.out.println("\uD83E\uDDE1 댓글 리스트에 들어가는 commentDTO = " + commentDTO);
            commentDTOList.add(commentDTO);

        }
        return commentDTOList;
    }

}