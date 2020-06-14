package core.mvc.view;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HandlerExecutionViewResolver extends AbstractViewResolver {
    @Override
    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        if (!(handler instanceof HandlerExecution)) {
            return null;
        }

        ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
        if (modelAndView.getView() == null) {
            modelAndView.setView(extractView(response, modelAndView));
        }

        return modelAndView;
    }

    private View extractView(final HttpServletResponse response,
                             final ModelAndView modelAndView) throws IOException {
        String viewName = modelAndView.getViewName();
        return resolve(viewName, response);
    }
}
