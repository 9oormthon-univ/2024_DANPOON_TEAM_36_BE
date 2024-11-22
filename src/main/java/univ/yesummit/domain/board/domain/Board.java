package univ.yesummit.domain.board.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.yesummit.domain.board.api.dto.request.BoardUpdateReqDto;
import univ.yesummit.domain.comment.domain.Comment;
import univ.yesummit.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    @Schema(description = "피칭 게시글 id", example = "1")
    private Long boardId;

    // 게시글 써밋 아이디 필드 만들기 (숫자만 저장하는 컬럼)

    @Schema(description = "피칭 제목", example = "제목")
    @NotNull(message = "필수 입력 항목입니다.")
    private String title;

    @Schema(description = "피칭 요약", example = "요약")
    @Column(columnDefinition = "TEXT")
    @NotNull(message = "필수 입력 항목입니다.")
    private String content;

    @Schema(description = "서비스 웹사이트 혹은 기타 링크(선택)", example = "사이트 링크")
    @Column(columnDefinition = "TEXT")
    private String serviceUrl;

    @Schema(description = "PT 영상 링크", example = "영상 링크")
    @Column(columnDefinition = "TEXT")
    @NotNull(message = "필수 입력 항목입니다.")
    private String PTUrl;

    @Column(name = "summitId")
    private Long summitId;

    @Schema(description = "게시글 날짜", example = "2024.06.21")
    private String boardDate;

    @Schema(description = "좋아요 개수", example = "1")
    private int likeCount;

    @Schema(description = "투자 제안한 횟수", example = "1")
    private int investmentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Schema(description = "작성자", example = "nickname")
    private Member writer;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    @Schema(description = "이미지")
    private List<BoardPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Board(String title, String content, String serviceUrl, String PTUrl, Member writer,Long isSummit) {
        this.title = title;
        this.content = content;
        this.serviceUrl = serviceUrl;
        this.PTUrl = PTUrl;
        this.boardDate = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        this.likeCount = 0;
        this.writer = writer;
        this.summitId = summitId;
    }

    public void boardUpdate(BoardUpdateReqDto boardUpdateReqDto) {
        this.title = boardUpdateReqDto.title();
        this.content = boardUpdateReqDto.content();
    }

    public void updateLikeCount() {
        this.likeCount++;
    }
    public void updateInvestmentCount() {
        this.investmentCount++;
    }

    public void cancelLikeCount() {
        if (this.likeCount <= 0) {
            this.likeCount = 0;
        } else {
            this.likeCount--;
        }
    }
    public void cancelInvestmentCount() {
        if (this.investmentCount <= 0) {
            this.investmentCount = 0;
        } else {
            this.investmentCount--;
        }
    }
}