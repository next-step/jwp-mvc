package core.mvc.tobe.handlermapping.custom;

import com.google.common.collect.Maps;
import core.mvc.asis.Controller;
import core.mvc.asis.ForwardController;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.HandlerKey;
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
    public HandlerExecutions init() {
        initializeUrlMappings();

        Map<HandlerKey, HandlerExecution> handlers = Maps.newHashMap();
        mappings.keySet().stream()
                .forEach(url -> handlers.put(createHandlerKey(url), createHandler(url)));

        return new HandlerExecutions(handlers);
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

    private void initializeUrlMappings() {
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
    }

    private HandlerExecution createHandler(String url) {
        Controller controller = mappings.get(url);
        return new HandlerExecution(controller);
    }

    private HandlerKey createHandlerKey(String url) {
        return new HandlerKey(url, null);
    }
}
