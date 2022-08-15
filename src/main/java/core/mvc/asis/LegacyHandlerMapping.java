package core.mvc.asis;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.HandlerMapping;
import next.controller.CreateUserController;
import next.controller.HomeController;
import next.controller.ListUserController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import next.controller.UpdateUserController;

public class LegacyHandlerMapping implements HandlerMapping {
	private static final Logger logger = LoggerFactory.getLogger(LegacyHandlerMapping.class);
	private Map<String, Controller> mappings = new HashMap<>();

	public LegacyHandlerMapping() {
		initMapping();
	}

	private void initMapping() {
		logger.info("Initialized Request Mapping!");

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

		mappings.keySet().forEach(path -> {
			logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
		});
	}

	private void put(String url, Controller controller) {
		mappings.put(url, controller);
	}

	@Override
	public Object getHandler(HttpServletRequest request) {
		return mappings.get(request.getRequestURI());
	}
}
