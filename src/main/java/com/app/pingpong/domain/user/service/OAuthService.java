package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.TokenRequest;
import com.app.pingpong.domain.user.dto.request.UserInfoRequest;
import com.app.pingpong.domain.user.dto.request.UserOauthRequest;
import com.app.pingpong.domain.user.dto.response.TokenResponse;
import com.app.pingpong.domain.user.dto.response.UserLoginResponse;
import com.app.pingpong.domain.user.dto.response.UserOAuthResponse;
import com.app.pingpong.domain.user.entity.RefreshToken;
import com.app.pingpong.domain.user.entity.SocialLoginType;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.RefreshTokenRepository;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.config.JwtTokenProvider;
import com.app.pingpong.global.exception.BaseException;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.app.pingpong.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String google_client_id;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String google_redirect_uri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String google_client_secret;

    @Value("${request-url.kakao}")
    private String kakao_request_url;

    public UserOAuthResponse getUserInfo(UserInfoRequest request) {
        String requestUrl;
        String etcUrl;
        UserOAuthResponse userInfo = null;

        switch (request.getSocialType()) {
            case "KAKAO": {
                requestUrl = "https://kauth.kakao.com/oauth/token";
                etcUrl = kakao_request_url + request.getCode();
                String accessToken = getUserAccessToken(requestUrl, etcUrl);
                userInfo = getKakaoUserInfo(accessToken);
                break;
            }

            case "GOOGLE": {
                requestUrl = "https://oauth2.googleapis.com/token";
                etcUrl = "code=" + request.getCode() + "&client_id=" + google_client_id + "&client_secret=" + google_client_secret + "&redirect_uri=" + google_redirect_uri + "&grant_type=authorization_code";
                break;
            }

            default:
                throw new BaseException(INVALID_SOCIAL_TYPE);
        }
        return userInfo;
    }

    private String getUserAccessToken(String requestUrl, String etcUrl) {
        String accessToken = "";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(etcUrl);
            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    private UserOAuthResponse getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String id = element.getAsJsonObject().get("id").getAsString();
            userInfo.put("email", email);
            userInfo.put("id", id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UserOAuthResponse((String)userInfo.get("id"), (String)userInfo.get("email"));
    }

    private UserOAuthResponse getGoogleUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://www.googleapis.com/oauth2/v2/userinfo";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);
            String email = element.getAsJsonObject().get("email").getAsString();
            String id = element.getAsJsonObject().get("id").getAsString();
            userInfo.put("email", email);
            userInfo.put("id", id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UserOAuthResponse((String)userInfo.get("id"), (String)userInfo.get("email"));
    }

    public UserLoginResponse login(UserOauthRequest request) {
        String email = request.getEmail();
        String socialIdx = request.getSocialIdx();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(EMAIL_NOT_FOUND));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, socialIdx);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponse.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return UserLoginResponse.of(user, tokenResponse);
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequest) {
        // 1. 리프레시 토큰 검증

        // 2. 액세스 토큰에서 유저 식별자 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // 3. 저장소에 있는 식별자 기반으로 리프레시 토큰을 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        // 4. 리프레시 토큰이 일치하는지 검사
        if(!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 토큰 재생성
        TokenResponse token = jwtTokenProvider.createToken(authentication);

        // 리프레시 토큰 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenRequest.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return token;
    }
}
