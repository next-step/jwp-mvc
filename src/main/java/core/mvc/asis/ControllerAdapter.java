package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ControllerAdapter implements HandlerExecution {

    private final Controller controller;

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = controller.execute(request, response);
        return new ModelAndView(viewName);
    }
}
