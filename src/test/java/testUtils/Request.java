package testUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Request {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "http://localhost:8080";

    public static ResponseEntity<String> get(String path) {
        final String url = baseUrl + path;
        return restTemplate.getForEntity(url, String.class);
    }

}
