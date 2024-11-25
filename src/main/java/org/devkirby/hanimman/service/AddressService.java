package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class AddressService {

    private static final String API_KEY = "d3f9d15a27f865ac85590c91e07a00a7"; // 발급받은 API 키

    public AddressDTO getAdministrativeArea(double lat, double lng) {
        String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%f&y=%f", lng, lat);
        StringBuilder result = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET"); // GET 방식으로 요청
            con.setRequestProperty("Authorization", "KakaoAK " + API_KEY);// API 키를 헤더에 추가

            int responseCode = con.getResponseCode(); // 응답 코드 확인
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답인 경우
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine); // 응답 내용을 읽어 문자열에 추가
                }
                in.close();
            } else {
                throw new RuntimeException("API 호출 오류: " + responseCode); // 오류 발생 시 예외 처리
            }
        } catch (Exception e) {
            // 로깅을 위한 코드 추가
            e.printStackTrace();
            throw new RuntimeException("API 호출 오류: " + e.getMessage());
        }

        // JSON 파싱
        JSONObject jsonResponse = new JSONObject(result.toString());
        JSONArray regions = jsonResponse.getJSONArray("documents");

        // 법정동 정보만 추출
        for (int i = 0; i < regions.length(); i++) {
            JSONObject region = regions.getJSONObject(i);
            String regionType = region.getString("region_type");
            if ("B".equals(regionType)) { // 법정동인 경우
                String id = region.getString("code");
                return AddressDTO.builder()
                        .id(region.getString("code"))
                        .cityName(region.getString("region_1depth_name"))
                        .districtName(region.getString("region_2depth_name"))
                        .neighborhoodName(region.getString("region_3depth_name"))
                        .villageName(region.optString("region_4depth_name", null))
                        .cityCode(id.substring(0, 2))
//                        .cityCode(region.optString("region_1depth_code"))
                        .districtCode(id.substring(2, 5))
//                        .districtCode(region.optString("region_2depth_code"))
                        .neighborhoodCode(region.optString("region_3depth_code"))
//                        .neighborhoodCode(id.substring(5, 3))
                        .villageCode(region.optString("region_4depth_code", null))

                        .build(); // DTO로 반환
            }
        }

        throw new RuntimeException("법정동 정보가 없습니다."); // 법정동 정보가 없는 경우 예외 처리
    }
}