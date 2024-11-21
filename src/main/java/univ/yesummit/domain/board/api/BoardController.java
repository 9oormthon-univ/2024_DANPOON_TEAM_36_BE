package univ.yesummit.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.board.api.dto.request.BoardSaveReqDto;
import univ.yesummit.domain.board.api.dto.request.BoardUpdateReqDto;
import univ.yesummit.domain.board.api.dto.response.BoardInfoResDto;
import univ.yesummit.domain.board.application.BoardService;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

import java.util.List;

@RestController
@RequestMapping("/v1/api/board")
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(summary = "PT 영상 및 정보 등록", description = "PT 영상 및 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping(value= "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> boardSave(@User LoginUser loginUser,
                                            @RequestBody BoardSaveReqDto boardSaveReqDto) {

        Long boardId = boardService.boardSave(loginUser.getMemberId(), boardSaveReqDto);
        String message = String.format("%d번 게시글 등록!", boardId);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @Operation(summary = "내가 작성한 PT 영상 게시글 조회", description = "특정 사용자가 작성한 PT 영상 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "게시글 없음", content = @Content(schema = @Schema(example = "게시글이 존재하지 않습니다."))),
    })
    @GetMapping("/my/{boardId}")
    public ResponseEntity<BoardInfoResDto> myBoardInfo(@User LoginUser loginUser, @PathVariable(name = "boardId") Long boardId) {
        BoardInfoResDto boardInfo = boardService.boardInfo(loginUser.getMemberId(), boardId);
        return new ResponseEntity<>(boardInfo, HttpStatus.OK);
    }

    @Operation(summary = "PT 영상 게시글 수정", description = "PT 영상 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "게시글 없음", content = @Content(schema = @Schema(example = "게시글이 존재하지 않습니다."))),
    })
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardInfoResDto> boardUpdate(@User LoginUser loginUser,
                                                                   @PathVariable(name = "boardId") Long boardId,
                                                                   @RequestBody BoardUpdateReqDto boardUpdateReqDto) {

        Board board = boardService.findById(boardId);
        boardService.boardUpdate(loginUser.getMemberId(), boardId, boardUpdateReqDto);
        //String message = String.format("%d번 게시글 수정 완료!", boardId);
        return new ResponseEntity<>(boardService.boardUpdate(loginUser.getMemberId(), boardId, boardUpdateReqDto), HttpStatus.OK);
    }

    @Operation(summary = "PT 영상 게시글 삭제", description = "PT 영상 게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "게시글 없음", content = @Content(schema = @Schema(example = "게시글이 존재하지 않습니다."))),
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> boardDelete(@User LoginUser loginUser,
                                            @PathVariable(name = "boardId") Long boardId) {
        boardService.boardDelete(loginUser.getMemberId(), boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}