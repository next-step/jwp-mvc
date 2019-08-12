package next.mock;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MockController {

    @RequestMapping(value = "/mock/users", method = RequestMethod.GET)
    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("mock", "users");
        return new ModelAndView("forward:users.jsp");
    }

    @RequestMapping(value = "/mock/qna", method = RequestMethod.POST)
    public ModelAndView qna(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("mock", "qna");
        return new ModelAndView("forward:qna.jsp");
    }

    @RequestMapping(value = "/mock/string", method = RequestMethod.GET)
    public String string(@RequestParam("string") String name, HttpServletRequest request) {
        request.setAttribute("mock", name);
        return "forward:test";
    }

}
