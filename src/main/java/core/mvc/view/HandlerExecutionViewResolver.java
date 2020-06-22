package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerExecutionViewResolver implements ViewResolver {

    @Override
    public ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ModelAndView mav = ((HandlerExecution) handler).handle(request, response);
        final View view = new JspView(getJspViewName(request.getRequestURI()));
        mav.setView(view);

        return mav;
    }

    private String getJspViewName(String requestURI) {
        return requestURI.endsWith(".jsp") ? requestURI : requestURI + ".jsp";
    }
}
