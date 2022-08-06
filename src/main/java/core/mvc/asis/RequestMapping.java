package core.mvc.asis;

import core.mvc.tobe.ControllerExecution;
import core.mvc.tobe.HandlerExecutable;
import core.mvc.tobe.HandlerMapping;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import next.controller.CreateUserController;
import next.controller.HomeController;
import next.controller.ListUserController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import next.controller.UpdateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(RequestMapping.class);
    private final Map<String, Controller> mappings = new HashMap<>();

    void initMapping() {
        mappings.put("/", new HomeController());
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new ListUserController());
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/create", new CreateUserController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass()));
    }

    public Controller findController(String url) {
        return mappings.get(url);
    }

    @Override
    public HandlerExecutable getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        if (mappings.containsKey(requestUri)) {
            return new ControllerExecution(mappings.get(requestUri));
        }

        throw new IllegalArgumentException("요청 uri에 해당하는 컨트롤러가 없습니다: " + requestUri);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
