package com.app.pingpong.domain.member.dto.response;

import com.app.pingpong.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponse {
    private Long memberId;
    private String nickname;
    private String profileImage;
    private int friendCount;

    public static MemberDetailResponse of(Member member, int friendCount) {
        return new MemberDetailResponse(member.getId(), member.getNickname(), member.getProfileImage(), friendCount);
    }
}
