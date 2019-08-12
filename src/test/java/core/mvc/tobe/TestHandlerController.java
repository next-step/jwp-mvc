package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestHandlerController {
    ModelAndView execution(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView();
    }
    void otherExecution() {
        System.out.println("wrong method");
    }
}
