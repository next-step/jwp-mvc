package core.mvc;

import core.annotation.web.RequestMethod;
import next.model.User;
import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractDispatcherServletTest {

    protected MockHttpServletRequest userPostRequest(String url, RequestMethod method, User user) {
        MockHttpServletRequest request = new MockHttpServletRequest(method.name(), url);
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        return request;
    }

    protected MockHttpServletRequest loginRequest(User user) {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.POST.name(), "/users/login");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        return request;
    }
}
