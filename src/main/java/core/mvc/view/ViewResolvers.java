package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/22 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class ViewResolvers {

    private static final String ILLEGAL_VIEW_RESOLVER = "요청에 맞는 ViewResolver를 찾을 수 없습니다.";

    private ControllerViewResolver cvr;
    private HandlerExecutionViewResolver hevr;

    public ViewResolvers() {
        this.cvr = new ControllerViewResolver();
        this.hevr = new HandlerExecutionViewResolver();
    }

    public ModelAndView resolve(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof Controller) {
            return cvr.resolve(handler, req, resp);
        }

        if (handler instanceof HandlerExecution) {
            return hevr.resolve(handler, req, resp);
        }

        throw new ServletException(ILLEGAL_VIEW_RESOLVER);
    }
}
