package com.app.pingpong.domain.member.service;

import com.app.pingpong.domain.friend.repository.FriendRepository;
import com.app.pingpong.domain.member.dto.request.SignUpRequest;
import com.app.pingpong.domain.member.dto.response.MemberResponse;
import com.app.pingpong.domain.member.entity.Authority;
import com.app.pingpong.domain.member.entity.Member;
import com.app.pingpong.domain.member.repository.MemberTeamRepository;
import com.app.pingpong.domain.s3.S3Uploader;
import com.app.pingpong.domain.team.repository.PlanRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.common.Status;
import com.app.pingpong.domain.member.repository.MemberRepository;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.util.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

import static com.app.pingpong.global.common.Status.ACTIVE;
import static com.app.pingpong.global.exception.StatusCode.SUCCESS_VALIDATE_NICKNAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private FriendRepository friendRepository;
    @Mock private MemberTeamRepository memberTeamRepository;
    @Mock private PlanRepository planRepository;

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private UserFacade userFacade;
    @Mock private PasswordEncoder passwordEncoder;

    @Mock private S3Uploader s3Uploader;
    @Mock private ListOperations<String, Object> listOps;

    @InjectMocks private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberService(memberRepository, friendRepository, memberTeamRepository, planRepository,
                redisTemplate, userFacade, passwordEncoder, s3Uploader);
    }

    @Test
    @DisplayName("회원가입")
    public void signup() throws NoSuchFieldException, IllegalAccessException {
        // given
        SignUpRequest request = new SignUpRequest("1234", "test@email.com", "nickname", "profileImage");
        Member member = request.toEntity(passwordEncoder);
        Field filed = member.getClass().getDeclaredField("id");
        filed.setAccessible(true);
        filed.set(member, 1L);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponse response = memberService.signup(request);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
        assertThat(response.getNickname()).isEqualTo(request.getNickname());
        assertThat(response.getProfileImage()).isEqualTo(request.getProfileImage());
    }

    @Test
    @DisplayName("회원가입 시 닉네임 유효성 테스트")
    public void validateNickname() {
        Member member1 = new Member("123", "email", "nickname", "profileImage", ACTIVE, Authority.ROLE_USER);
        Member member2 = new Member("123", "email", "nickname", "profileImage", ACTIVE, Authority.ROLE_USER);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 유효한 닉네임인 경우 SUCCESS_VALIDATE_NICKNAME, 200을 리턴함
        String validNickname = member1.getNickname();
        BaseResponse<String> response = memberService.validateNickname(validNickname);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals(SUCCESS_VALIDATE_NICKNAME.getMessage(), response.getMessage());

        // 이미 존재하는 닉네임인 경우 BaseException을 Throw
        String duplicateNickname = member2.getNickname();
        given(memberRepository.existsMemberByNicknameAndStatus(duplicateNickname)).willReturn(true);
        assertThrows(BaseException.class, () -> memberService.validateNickname(duplicateNickname));

        // 유효하지 않은 닉네임인 경우 BaseException을 Throw
        String invalidNickname = "@!#dsaflah11dfadfa";
        assertThrows(BaseException.class, () -> memberService.validateNickname(invalidNickname));
    }

    private Member createMember() {
        return new Member("123", "email", "nickname", "profileImage", ACTIVE, Authority.ROLE_USER);
    }
}