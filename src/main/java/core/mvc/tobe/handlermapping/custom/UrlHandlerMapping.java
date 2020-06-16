package core.mvc.tobe.handlermapping.custom;

import core.mvc.asis.Controller;
import core.mvc.asis.ForwardController;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handlermapping.HandlerMapping;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UrlHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(UrlHandlerMapping.class);

    private Map<String, Controller> mappings = new HashMap<>();

    @Override
    public void init() {
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
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    @Override
    public HandlerExecution findHandler(HttpServletRequest request) {
        Controller controller = mappings.get(request.getRequestURI());
        return new HandlerExecution(controller);


    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return findHandler(request).getController() != null;
    }
}
