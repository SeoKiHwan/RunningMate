package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.dto.BoardDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.service.BoardService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    //게시글 생성
    @PostMapping("/boards")
    public Long save(HttpServletRequest request, @RequestBody BoardDto boardDto) {
        UserDto userDto = userService.getUserByToken(request);

        return boardService.save(boardDto, userDto);
    }

    //게시글 수정
    @PostMapping("/boards/{board-id}")
    public ResponseEntity<String> update(@PathVariable("board-id") String boardId, @RequestBody BoardDto boardDto) {
        boardService.update(Long.parseLong(boardId), boardDto);

        return ResponseEntity.ok("수정 완료");
    }

    //게시글 조회(단 건의 게시글)
    @GetMapping("/boards/{board-id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable("board-id") String boardId, HttpServletRequest request) {
        UserDto userDto = userService.getUserByToken(request);
        BoardDto boardDto = boardService.findBoard(Long.parseLong(boardId), userDto);

        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    //게시글 상태 변화
    @PatchMapping("/boards/{board-id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable("board-id") String boardId) {
        Boolean status = boardService.updateStatus(Long.parseLong(boardId));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    //내가 작성한 글 조회
    @GetMapping("/users/{user-name}/boards")
    public ResponseEntity<List<BoardDto>> findMyBoard(HttpServletRequest request,
                                                      @RequestParam("offset") int offset,
                                                      @RequestParam("limit") int limit) {
        UserDto userDto = userService.getUserByToken(request);

        List<BoardDto> myBoardList = boardService.findMyBoardList(userDto, offset, limit);

        return new ResponseEntity<>(myBoardList, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("/boards/{board-id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("board-id") String boardId) {
        boardService.deleteBoard(Long.parseLong(boardId));

        return ResponseEntity.ok("삭제 완료");
    }

    //게시글 조회(리스트, 주소 별로 조회)
    @GetMapping("/boards")
    public ResponseEntity<List<BoardDto>> findAllBoard(@RequestParam(value = "dou", required = false) String dou,
                                                       @RequestParam(value = "si", required = false) String si,
                                                       @RequestParam(value = "gu", required = false) String gu,
                                                       @RequestParam(value = "offset", required = false) int offset,
                                                       @RequestParam(value = "limit", required = false) int limit) {
        List<BoardDto> boardList = boardService.findBoardList(dou, si, gu, offset, limit);

        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }
}