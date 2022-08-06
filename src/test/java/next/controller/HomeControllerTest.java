package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class HomeControllerTest {

    private static final String URL = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();

    @DisplayName("초기 화면")
    @Test
    void home() {
        final ResponseEntity<String> forEntity = restTemplate.getForEntity(URL + "/", String.class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
