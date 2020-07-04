package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.resolver.view.StringPathViewResolver;
import core.mvc.tobe.resolver.view.ViewResolver;
import core.mvc.tobe.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandlerAdapter implements HandlerCommand {

    private Controller controller;

    public RequestHandlerAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = controller.execute(request, response);

        ViewResolver viewResolver = new StringPathViewResolver();
        View view = viewResolver.resolveViewName(viewName);
        return new ModelAndView(view);
    }
}
