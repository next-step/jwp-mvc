package core.mvc.asis;

import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.HandlerMethod;
import lombok.extern.slf4j.Slf4j;
import next.controller.CreateUserController;
import next.controller.ListUserController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import next.controller.UpdateUserController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestMapping implements HandlerMapping {

    private Map<String, Controller> mappings = new HashMap<>();

    @Override
    public void initialize() {
//        mappings.put("/", new HomeController());
//        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
//        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new ListUserController());
//        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
//        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/create", new CreateUserController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        log.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            log.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return this.mappings.containsKey(request.getRequestURI());
    }

    @Override
    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        try {
            String requestUri = request.getRequestURI();
            Controller controller = findController(requestUri);
            Method method = controller.getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);

            return new HandlerMethod(method, requestUri);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No such method on interface Controller", e);
        }
    }

    public Controller findController(String uri) {
        return mappings.get(uri);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
