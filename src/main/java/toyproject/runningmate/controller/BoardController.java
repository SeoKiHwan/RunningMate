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
    @PostMapping("/run")
    public Long save(HttpServletRequest request, @RequestBody BoardDto boardDto) {
        UserDto userDto = userService.getUserByToken(request);

        return boardService.save(boardDto, userDto);
    }

    //게시글 수정
    @PostMapping("/run/{boardId}")
    public ResponseEntity<String> update(@PathVariable("boardId") String boardId, @RequestBody BoardDto boardDto) {
        boardService.update(Long.parseLong(boardId), boardDto);

        return ResponseEntity.ok("수정 완료");
    }

    //게시글 조회
    @GetMapping("/run/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable("boardId") String boardId, HttpServletRequest request) {
        UserDto userDto = userService.getUserByToken(request);
        BoardDto boardDto = boardService.findBoard(Long.parseLong(boardId), userDto);

        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    //게시글 상태 변화
    @PatchMapping("/run/{boardId}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable("boardId") String boardId) {
        Boolean status = boardService.updateStatus(Long.parseLong(boardId));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    //내가 작성한 글 조회
    @GetMapping("/myrun")
    public ResponseEntity<List<BoardDto>> findMyBoard(HttpServletRequest request,
                                                      @RequestParam("offset") int offset,
                                                      @RequestParam("limit") int limit) {
        UserDto userDto = userService.getUserByToken(request);

        List<BoardDto> myBoardList = boardService.findMyBoardList(userDto, offset, limit);

        return new ResponseEntity<>(myBoardList, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("/run/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") String boardId) {
        boardService.deleteBoard(Long.parseLong(boardId));

        return ResponseEntity.ok("삭제 완료");
    }

    //게시글 조회
    @GetMapping("/runs")
    public ResponseEntity<List<BoardDto>> findAllBoard(@RequestParam(value = "si", required = false) String si,
                                                       @RequestParam(value = "gu", required = false) String gu,
                                                       @RequestParam(value = "dong", required = false) String dong,
                                                       @RequestParam(value = "offset", required = false) int offset,
                                                       @RequestParam(value = "limit", required = false) int limit) {
        List<BoardDto> boardList = boardService.findBoardList(si, gu, dong, offset, limit);

        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }
}
