package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller("/users")
public class UserController  {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView createUserForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logging("create user form");

        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    private void logging(String requestName) {
        log.info("Annotation Mapping Handler {} Complete", requestName);
    }
}
