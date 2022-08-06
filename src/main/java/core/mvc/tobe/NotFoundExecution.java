package core.mvc.tobe;

import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotFoundExecution implements HandlerExecutable {

    @Override
    public boolean executable() {
        return true;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return ModelAndView.jsp("404.jsp");
    }
}
