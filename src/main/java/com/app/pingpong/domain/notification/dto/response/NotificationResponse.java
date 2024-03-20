package com.app.pingpong.domain.notification.dto.response;

import com.app.pingpong.domain.member.entity.Member;
import com.app.pingpong.domain.notification.entity.Notification;
import com.app.pingpong.global.common.status.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationResponse {
    private String notificationId;
    private Status type;
    private Long memberId; // 나한테 초대를 보낸 멤버
    private String profileImage;
    private Long teamId;
    private String message;
    private Boolean isClicked;
    private Boolean isAccepted;

    public static NotificationResponse of(Notification notification, Member member) {
        return new NotificationResponse(notification.getId(), notification.getType(), member.getId(),
                member.getProfileImage(), notification.getTeamId(), notification.getMessage(), notification.getIsClicked(), notification.getIsAccepted());
    }
}
