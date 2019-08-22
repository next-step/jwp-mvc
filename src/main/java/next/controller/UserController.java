package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  @RequestMapping("/users/create")
  public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    User user = new User(req.getParameter("userId"), req.getParameter("password"),
        req.getParameter("name"),
        req.getParameter("email"));
    log.debug("User : {}", user);

    DataBase.addUser(user);
    return new ModelAndView(new RedirectView("/"));
  }

  @RequestMapping("/users")
  public ModelAndView userList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return new ModelAndView(new RedirectView("/users/loginForm"));
    }
    ModelAndView modelAndView = new ModelAndView(new JspView("/user/list"));
    modelAndView.addObject("users", DataBase.findAll());
    return modelAndView;
  }

}
