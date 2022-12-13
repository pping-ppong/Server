package com.app.pingpong.domain.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResponse {
    private Long searchIdx;
    private String keyword;
}
