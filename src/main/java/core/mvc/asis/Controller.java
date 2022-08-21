package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.ExecuteHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Controller implements ExecuteHandler {

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(execute(request, response));
    }
    public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
