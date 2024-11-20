package univ.yesummit.domain.feed.dto;

import java.util.List;

public record FeedListResponse(
        List<FeedResponse> feeds
) {
}
