package univ.yesummit.domain.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.yesummit.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(name = "feed_image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Feed(String image, Member member) {
        this.image = image;
        this.member = member;
    }
}
