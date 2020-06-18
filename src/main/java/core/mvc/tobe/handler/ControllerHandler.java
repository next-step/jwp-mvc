package core.mvc.tobe.handler;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.ModelAndViewGettable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandler implements Handler<Controller>, ModelAndViewGettable {
    @Override
    public ModelAndView handle(Controller handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getModelAndView(request, handler.execute(request, response));
    }
}