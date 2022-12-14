package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.oauth.TokenRequest;
import com.app.pingpong.domain.user.dto.request.oauth.UserInfoRequest;
import com.app.pingpong.domain.user.dto.request.oauth.UserOauthRequest;
import com.app.pingpong.domain.user.dto.response.TokenResponse;
import com.app.pingpong.domain.user.dto.response.UserLoginResponse;
import com.app.pingpong.domain.user.dto.response.UserOAuthResponse;
import com.app.pingpong.domain.user.entity.RefreshToken;
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

    @Value("${request-url.google}")
    private String google_request_url;

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
                String accessToken = getAccessToken(requestUrl, etcUrl);
                userInfo = getKakaoUserInfo(accessToken);
                break;
            }

            case "GOOGLE": {
                requestUrl = "https://oauth2.googleapis.com/token";
                etcUrl = "code=" + request.getCode() + google_request_url;
                String accessToken = getAccessToken(requestUrl, etcUrl);
                userInfo = getGoogleUserInfo(accessToken);
                break;
            }

            default:
                throw new BaseException(INVALID_SOCIAL_TYPE);
        }

        return userInfo;
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

    private String getAccessToken(String requestUrl, String etcUrl) {
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



    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequest) {
        // 1. ???????????? ?????? ??????

        // 2. ????????? ???????????? ?????? ????????? ????????????
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // 3. ???????????? ?????? ????????? ???????????? ???????????? ????????? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("??????????????? ??????????????????."));

        // 4. ???????????? ????????? ??????????????? ??????
        if(!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // ?????? ?????????
        TokenResponse token = jwtTokenProvider.createToken(authentication);

        // ???????????? ?????? ????????????
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenRequest.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return token;
    }
}
