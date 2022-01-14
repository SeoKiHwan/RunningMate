package toyproject.runningmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.Board.Board;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.BoardDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.BoardRepository;
import toyproject.runningmate.repository.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final EntityManager em;
    private final UserRepository userRepository;

    //게시글 생성
    @Transactional
    public Long save(BoardDto boardDto, UserDto userDto) {
        User user = userRepository.findByNickName(userDto.getNickName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        Board board = Board.builder()
                .user(user)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .isClosed(boardDto.isClosed())
                .meetingTime(boardDto.getMeetingTime())
                .address(boardDto.getAddress())
                .regDate(LocalDateTime.now())
                .count(boardDto.getCount())
                .image(boardDto.getImage())
                .openChat(boardDto.getOpenChat())
                .build();

        boardRepository.save(board);

        return board.getId();
    }

    //게시글 수정
    @Transactional
    public void update(Long boardId, BoardDto boardDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 존재 X"));

        board.update(
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getAddress(),
                boardDto.getMeetingTime(),
                boardDto.getImage(),
                boardDto.getOpenChat()
        );
    }

    //게시글 조회
    @Transactional
    public BoardDto findBoard(Long boardId, UserDto userDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 존재 X"));

        if(!(board.getUser().getId().equals(userDto.getId()))){
            board.updateCount();
        }

        return board.toBoardDto();
    }

    //게시글 상태 변경
    @Transactional
    public Boolean updateStatus(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 존재 X"));

        return board.changeIsClosed(!board.isClosed());
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 존재 X"));

        boardRepository.delete(board);
    }

    //내 작성글 조회
    public List<BoardDto> findMyBoardList(UserDto userDto, int offset, int limit) {
        Long userId = userDto.getId();

        return em.createQuery(
                "select b from Board b" +
                        " join b.user u on u.id = :id", Board.class)
                .setParameter("id", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList().stream()
                .map(bl -> bl.toBoardDto())
                .collect(Collectors.toList());
    }

    public List<BoardDto> findBoardList(String si, String gu, String dong, int offset, int limit) {

        if (si == null) {
            return em.createQuery("select b from Board b", Board.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList().stream()
                    .map(bl -> bl.toBoardDto())
                    .collect(Collectors.toList());
        } else if (gu == null) {
            return em.createQuery("select b from Board b where b.address.si = :si", Board.class)
                    .setParameter("si", si)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList().stream()
                    .map(bl -> bl.toBoardDto())
                    .collect(Collectors.toList());
        } else if (dong == null) {
            return em.createQuery("select b from Board b where b.address.si = :si and b.address.gu = :gu", Board.class)
                    .setParameter("si", si)
                    .setParameter("gu", gu)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList().stream()
                    .map(bl -> bl.toBoardDto())
                    .collect(Collectors.toList());
        } else {
            return em.createQuery("select b from Board b where b.address.si = :si and b.address.gu = :gu and b.address.dong = :dong", Board.class)
                    .setParameter("si", si)
                    .setParameter("gu", gu)
                    .setParameter("dong", dong)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList().stream()
                    .map(bl -> bl.toBoardDto())
                    .collect(Collectors.toList());
        }
    }
}
