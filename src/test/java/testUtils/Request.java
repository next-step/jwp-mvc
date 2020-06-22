package testUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Request {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "http://localhost:8080";

    public static ResponseEntity<String> get(String path) {
        final String url = baseUrl + path;
        return restTemplate.getForEntity(url, String.class);
    }

    public static ResponseEntity<String> submitForm(String path, MultiValueMap<String, String> body) {
        final String url = baseUrl + path;
        final HttpHeaders headers = new HttpHeaders();

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(url, request, String.class);
    }

}
