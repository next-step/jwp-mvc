package next.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import testUtils.Request;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthControllerTest {

    private static final String USER_ID = "user";
    private static final String PASSWORD = "pass";

    @BeforeAll
    static void setUp() {
        createUser(USER_ID, PASSWORD);
    }

    private static ResponseEntity<String> createUser(String userId, String pass) {
        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", userId);
        form.add("password", pass);

        return Request.submitForm("/users/create", form);
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 할 경우 재로그인 문구를 알린다")
    void notExistUserId() {
        final String notExistUserId = "notnotnot";

        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", notExistUserId);
        form.add("password", "a");

        final ResponseEntity<String> result = Request.submitForm("/users/login", form);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
    }

    @Test
    @DisplayName("일치하지 않는 패스워드로 로그인 할 경우 재로그인 문구를 알린다")
    void notMatchPassword() {
        final String notMatchPassword = "notnotnot";

        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", USER_ID);
        form.add("password", notMatchPassword);

        final ResponseEntity<String> result = Request.submitForm("/users/login", form);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
    }

    @Test
    @DisplayName("정상적으로 로그인 할 경우 메인 페이지로 이동한다")
    void loginSuccess() {
        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", USER_ID);
        form.add("password", PASSWORD);

        final ResponseEntity<String> result = Request.submitForm("/users/login", form);

        assertThat(result.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(result.getHeaders().getLocation().getPath()).isEqualTo("/");
    }

}
