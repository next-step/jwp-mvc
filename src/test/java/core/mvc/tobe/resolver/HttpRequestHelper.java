package core.mvc.tobe.resolver;

import org.springframework.mock.web.MockHttpServletRequest;

public class HttpRequestHelper {

    public static MockHttpServletRequest helper() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        request.addParameter("age", "45");

        return request;
    }
}
