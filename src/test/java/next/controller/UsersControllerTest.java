package next.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class UsersControllerTest {


    private static final String URL = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();

    @DisplayName("회원가입 화면")
    @Test
    void form() {
        final ResponseEntity<String> response = restTemplate.getForEntity(URL + "/users/form", String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<form name=\"question\" method=\"post\" action=\"/users/create\">"),
            () -> assertThat(response.getBody()).contains("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">회원가입</button>")
        );
    }

    @DisplayName("로그인 화면")
    @Test
    void loginForm() {
        final ResponseEntity<String> response = restTemplate.getForEntity(URL + "/users/loginForm", String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<form name=\"question\" method=\"post\" action=\"/users/login\">"),
            () -> assertThat(response.getBody()).contains("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">로그인</button>")
        );
    }

    @DisplayName("비 로그인 상태에서 사용자 목록 조회")
    @Test
    void users_without_login() {
        final ResponseEntity<String> response = restTemplate.getForEntity(URL + "/users", String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<form name=\"question\" method=\"post\" action=\"/users/login\">"),
            () -> assertThat(response.getBody()).contains("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">로그인</button>")
        );
    }

    @DisplayName("로그인 상태에서 사용자 목록 조회")
    @Test
    void users_with_login() {
        final ResponseEntity<String> loginResponse = 로그인_요청("admin", "password");

        final String sessionId = getSessionId(loginResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + sessionId);

        final ResponseEntity<String> response = restTemplate.exchange(URL + "/users", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<td>자바지기</td>"),
            () -> assertThat(response.getBody()).contains("<td>admin@slipp.net</td>")
        );
    }


    @DisplayName("로그인")
    @Test
    void login() {
        final ResponseEntity<String> response = 로그인_요청("admin", "password");

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND),
            () -> assertThat(response.getHeaders()).containsEntry("Location", List.of("/"))
        );

    }

    private ResponseEntity<String> 로그인_요청(final String userId, final String password) {
        final String loginParams = String.format("userId=%s&password=%s", userId, password);

        return restTemplate.postForEntity(URL + "/users/login?" + loginParams, new HttpEntity<>(null), String.class);
    }

    @DisplayName("회원 프로필 조회")
    @Test
    void profile() {
        final ResponseEntity<String> response = restTemplate.getForEntity(URL + "/users/profile?userId=admin", String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<h4>Profiles</h4>"),
            () -> assertThat(response.getBody()).contains("<h4 class=\"media-heading\">자바지기</h4>")
        );
    }

    @DisplayName("로그아웃")
    @Test
    void logout() {
        final ResponseEntity<String> response = restTemplate.getForEntity(URL + "/users/logout", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("회원가입")
    @Test
    void create() {
        final String createUserParams = String.format("userId=%s&password=%s&name=administrator&email=admin@email.com", "admin2", "password");

        final ResponseEntity<String> response = restTemplate.postForEntity(URL + "/users/create?" + createUserParams, new HttpEntity<>(null),
            String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND),
            () -> assertThat(response.getHeaders()).containsEntry("Location", List.of("/"))
        );

    }

    @DisplayName("회원 정보 수정 화면")
    @Test
    void updateForm() {
        final ResponseEntity<String> loginResponse = 로그인_요청("admin", "password");

        final String sessionId = getSessionId(loginResponse);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + sessionId);

        final ResponseEntity<String> response = restTemplate.exchange(URL + "/users/updateForm?userId=admin", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).contains("<form name=\"question\" method=\"post\" action=\"/users/update\">"),
            () -> assertThat(response.getBody()).contains("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">개인정보수정</button>")
        );
    }

    @DisplayName("회원 정보 수정")
    @Test
    void update() {
        final ResponseEntity<String> loginResponse = 로그인_요청("admin", "password");

        final String sessionId = getSessionId(loginResponse);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + sessionId);

        final String updateUserParams = String.format("userId=%s&password=%s&name=자바지기&email=admin@slipp.net", "admin", "password");

        final ResponseEntity<String> response = restTemplate.exchange(URL + "/users/update?" + updateUserParams, HttpMethod.POST, new HttpEntity<>(headers),
            String.class);

        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND),
            () -> assertThat(response.getHeaders()).containsEntry("Location", List.of("/"))
        );
    }

    private static String getSessionId(final ResponseEntity<String> loginResponse) {
        return Objects.requireNonNull(loginResponse.getHeaders().get("Set-Cookie")).stream()
            .filter(cookie -> cookie.startsWith("JSESSIONID"))
            .findFirst()
            .map(cookie -> cookie.split(";")[0])
            .map(cookie -> cookie.split("=")[1])
            .orElseThrow(() -> new RuntimeException("Cookie not found"));
    }
}
