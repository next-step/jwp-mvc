package next.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

public class UserAcceptanceTest {
    @DisplayName("/users/create 요청으로 회원가입 성공 ")
    @Test
    void test_create() {
        // given
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("userId", "crystal");
        body.add("password", "password");
        body.add("name", "임수정");
        body.add("email", "crystal@naver.com");
        // when // then
        EntityExchangeResult<byte[]> result = client()
                .post()
                .uri("/users/create")
                .body(BodyInserters.fromFormData(body))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .returnResult();
    }

    private WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
