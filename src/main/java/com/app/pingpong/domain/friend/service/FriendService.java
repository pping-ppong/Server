package com.app.pingpong.domain.friend.service;

import com.app.pingpong.domain.friend.dto.request.FriendRequest;
import com.app.pingpong.domain.friend.dto.response.FriendResponse;
import com.app.pingpong.domain.friend.entity.Friend;
import com.app.pingpong.domain.friend.repository.FriendRepository;
import com.app.pingpong.domain.member.entity.Member;
import com.app.pingpong.domain.member.repository.MemberRepository;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.StatusCode;
import com.app.pingpong.global.util.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.app.pingpong.global.common.Status.*;
import static com.app.pingpong.global.exception.StatusCode.*;

@RequiredArgsConstructor
@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final MemberFacade userFacade;

    public FriendResponse add(FriendRequest request) {
        Member applicant = memberRepository.findByIdAndStatus(request.getApplicantId(), ACTIVE).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        Member respondent = memberRepository.findByIdAndStatus(request.getRespondentId(), ACTIVE).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        checkFriendRequest(applicant, respondent);
        return FriendResponse.of(friendRepository.save(request.toEntity(applicant, respondent)));
    }

    @Transactional
    public StatusCode accept(Long opponentId) {
        Friend request = getWaitingFriendRequest(opponentId);
        checkAndSetStatusActive(request);
        return SUCCESS_ACCEPT_FRIEND;
    }

    @Transactional
    public StatusCode refuse(Long opponentId) {
        Friend request = getWaitingFriendRequest(opponentId);
        checkAndSetStatusDelete(request);
        return SUCCESS_REFUSE_FRIEND;
    }

    private void checkFriendRequest(Member applicant, Member respondent) {
        if (friendRepository.existsWaitRequestByApplicantIdAndRespondentId(applicant.getId(), respondent.getId())) {
            throw new BaseException(USER_ALREADY_FRIEND_REQUEST);
        }
        if (friendRepository.existsWaitRequestByApplicantIdAndRespondentId(respondent.getId(), applicant.getId())) {
            throw new BaseException(USER_ALREADY_GET_FRIEND_REQUEST);
        }
        if (friendRepository.existsActiveRequestByApplicantIdAndRespondentId(applicant.getId(), respondent.getId())) {
            throw new BaseException(ALREADY_ON_FRIEND);
        }
    }

    private Friend getWaitingFriendRequest(Long opponentId) {
        Member loginUser = userFacade.getCurrentMember();
        Friend friend = friendRepository.findWaitRequestByApplicantIdAndRespondentId(opponentId, loginUser.getId()).orElseThrow(() -> new BaseException(FRIEND_NOT_FOUND));
        return friend;
    }

    private void checkAndSetStatusActive(Friend friend) {
        if (friend.getStatus().equals(ACTIVE)) {
            throw new BaseException(ALREADY_ON_FRIEND);
        }
        friend.setStatus(ACTIVE);
    }

    private void checkAndSetStatusDelete(Friend friend) {
        if (friend.getStatus().equals(ACTIVE)) {
            throw new BaseException(ALREADY_ON_FRIEND);
        }
        friend.setStatus(DELETE);
    }
}
