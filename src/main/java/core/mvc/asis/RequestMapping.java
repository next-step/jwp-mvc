package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Added interface for compatibility with new module (AnnotationHandlerMapping)
 * <p>
 * This module has been deprecated. See the link below:
 * {@link core.mvc.tobe.AnnotationHandlerMapping}
 */
@Deprecated
public class RequestMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        mappings.put("/", new HomeController());
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new ListUserController());
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
        // mappings.put("/users/create", new CreateUserController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    /**
     * See the link below:
     * {@link RequestMapping#getHandler(HttpServletRequest)}
     */
    @Deprecated
    public Controller findController(String url) {
        return mappings.get(url);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) throws HandlerNotFoundException {
        final String requestUri = request.getRequestURI();
        return Optional
                .ofNullable(mappings.get(requestUri))
                .orElseThrow(() -> new HandlerNotFoundException("핸들러가 존재하지 않아요!"));
    }
}
