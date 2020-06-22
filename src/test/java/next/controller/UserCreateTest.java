package next.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import testUtils.Request;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreateTest {

    @Test
    @DisplayName("유저 생성이 성공하면 메인 페이지로 리다이렉트 한다")
    void create() {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userId", "bactoria");
        formData.add("password", "qwe123");
        formData.add("name", "bactoria");
        formData.add("email", "bactoria@test.com");

        final ResponseEntity<String> response = Request.submitForm("/users/create   ", formData);

        final String expectedLocation = "/";

        assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(response.getHeaders().getLocation()).isEqualTo(expectedLocation);
    }


}
