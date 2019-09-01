package core.mvc.asis;

import core.mvc.HandlerAdapter;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : yusik
 * @date : 2019-08-14
 */
public class LegacyHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(viewName);
    }
}
