package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.ForwardController;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ForwardController);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Assert.notNull(handler, "Handler가 null이어선 안됩니다.");
        ForwardController controller = (ForwardController) handler;
        return controller.execute(request, response);
    }
}
