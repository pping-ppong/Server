package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.response.UserLoginResponse;
import com.app.pingpong.domain.user.dto.response.UserOAuthResponse;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.config.JwtTokenProvider;
import com.app.pingpong.global.exception.user.EmailAlreadyExistsException;
import com.app.pingpong.global.utils.SecurityUtils;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakao_client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakao_redirect_uri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String google_client_id;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String google_redirect_uri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String google_client_secret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String google_scope;

    /* 액세스 토큰 발급 */
    public String getKakaoAccessToken (String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            //sb.append("&client_id=1f6fddb56b60c6bedd26a879812f0a7c"); // TODO REST_API_KEY 입력
            //sb.append("&redirect_uri=http://localhost:8080/app/users/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&client_id="+kakao_client_id);
            sb.append("&redirect_uri="+kakao_redirect_uri);
            sb.append("&code=" + code);
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
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /* 사용자 정보를 가져옴 : 소설식별자, 이메일 */
    public UserOAuthResponse getKakaoUserInfo(String accessToken) {
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

    public String getGoogleAccessToken (String code) {
        String accessToken = "";
        String reqURL = "https://oauth2.googleapis.com/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("code=" + code);
            sb.append("&client_id=" + google_client_id);
            sb.append("&client_secret=" + google_client_secret);
            sb.append("&redirect_uri=" + google_redirect_uri);
            sb.append("&grant_type=authorization_code");
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

    public UserOAuthResponse getGoogleUserInfo(String accessToken) {
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

    public UserLoginResponse kakaoLogin(String accessToken) {
        // 1. 액세스 토큰을 통해 유저의 정보를 가져옴
        UserOAuthResponse kakaoUserInfo = getKakaoUserInfo(accessToken);
        String email = kakaoUserInfo.getEmail();

        User user;
        // 가입된 유저가 아니라면 DB에 저장 -> 닉네임과 프로필 사진이 null이면 회원가입을 진행하도록 클라이언트에게 알림
        if (!userRepository.existsUserByEmail(email)) {
            user = userRepository.save(User.builder()
                    .socialIdx(Long.valueOf(kakaoUserInfo.getSocialIdx()))
                    .email(kakaoUserInfo.getEmail())
                    .nickname(null)
                    .profileImage(null)
                    .build()
            );
        }
        else { // 저장 되어있다면?
            user = userRepository.findByEmail(email).orElseThrow(() -> new EmailAlreadyExistsException());
        }
        // 토큰 발급
        String jwt = jwtTokenProvider.createAccessToken(user.getEmail());
        return new UserLoginResponse(user.getUserIdx(), user.getSocialIdx(), user.getEmail(),
                user.getNickname(), user.getProfileImage(), jwt);
    }
}
