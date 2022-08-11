package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller("/users")
public class UserController  {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView createUserForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logging("create user form");

        return createModelAndView("/user/form.jsp");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logging("create user");

        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return createModelAndView("redirect:/");
    }

    private ModelAndView createModelAndView(String path) {
        return new ModelAndView(new JspView(path));
    }

    private void logging(String requestName) {
        log.info("Annotation Mapping Handler {} Complete", requestName);
    }
}
