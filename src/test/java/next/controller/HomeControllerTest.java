package next.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import testUtils.Request;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeControllerTest {

    @Test
    @DisplayName("/ 로 접속 할 경우 text/html 리소스를 반환한다")
    void home() {
        final ResponseEntity<String> result = Request.get("/");

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getHeaders().getContentType().getType()).isEqualTo("text");
        assertThat(result.getHeaders().getContentType().getSubtype()).isEqualTo("html");
    }

}
