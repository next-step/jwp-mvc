package core.mvc.asis;

import core.mvc.Mapping;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import next.controller.HomeController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import next.controller.UpdateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping implements Mapping {

  private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
  private Map<String, Controller> mappings = new HashMap<>();

  public void initMapping() {
    mappings.put("/", new HomeController());
    mappings.put("/users/form", new ForwardController("/user/form.jsp"));
    mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
    mappings.put("/users/login", new LoginController());
    mappings.put("/users/profile", new ProfileController());
    mappings.put("/users/logout", new LogoutController());
    mappings.put("/users/updateForm", new UpdateFormUserController());
    mappings.put("/users/update", new UpdateUserController());

    logger.info("Initialized Request Mapping!");
    mappings.keySet().forEach(path -> {
      logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
    });
  }

  @Override
  public Controller findController(HttpServletRequest request) {
    return mappings.get(request.getRequestURI());
  }

  void put(String url, Controller controller) {
    mappings.put(url, controller);
  }
}
