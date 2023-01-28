package com.app.pingpong.domain.social.entity;

import com.app.pingpong.domain.social.dto.response.MemberInfoResponse;

public interface SocialOAuth {
    String getAccessToken(String code);
    MemberInfoResponse getUserInfo(String accessToken);
}
