package univ.yesummit.domain.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.yesummit.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_id")
    private Long investmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Investment(Board board, Member member) {
        this.board = board;
        this.member = member;
    }
}
