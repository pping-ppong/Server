package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.response.KakaoResponse;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class KakaoService {

    private final UserRepository userRepository;

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
            sb.append("&client_id=1f6fddb56b60c6bedd26a879812f0a7c"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8080/app/users/kakao"); // TODO 인가코드 받은 redirect_uri 입력
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
    public KakaoResponse getKakaoUserInfo(String accessToken) {
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
        return new KakaoResponse((String)userInfo.get("id"), (String)userInfo.get("email"));
    }

}
