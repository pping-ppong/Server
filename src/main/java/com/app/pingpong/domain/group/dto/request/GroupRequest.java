package com.app.pingpong.domain.group.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    private String groupName;
    private List<Long> memberIdx;

    /*
    public Group toEntity(User u) {
        return Group.builder()
                .name(groupName)
                //.host()
                //.user()
                .build();
    } */
}
