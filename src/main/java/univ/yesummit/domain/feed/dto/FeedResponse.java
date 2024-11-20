package univ.yesummit.domain.feed.dto;

import java.util.List;

public record FeedResponse(
        List<String> imageUrls
) {
}
