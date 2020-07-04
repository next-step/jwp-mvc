package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.resolver.view.ViewResolveParameter;
import core.mvc.tobe.resolver.view.ViewResolver;
import core.mvc.tobe.resolver.view.ViewResolvers;
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

        ViewResolveParameter parameter = new ViewResolveParameter(viewName);
        ViewResolver viewResolver = ViewResolvers.getResolver(parameter);
        View view = viewResolver.resolve(parameter);
        return new ModelAndView(view);
    }
}
