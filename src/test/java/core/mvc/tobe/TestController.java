package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/request-method-test")
    public ModelAndView requestMethodTest(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("TEST", "", "", "");
        DataBase.addUser(user);
        logger.debug("run requestMethodTest");
        return null;
    }

}
