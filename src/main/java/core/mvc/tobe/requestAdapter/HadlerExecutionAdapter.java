package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class HadlerExecutionAdapter implements HandlerAdapter {
    @Override
    public boolean isSupport(Object executor) {
        return executor instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object executor) throws Exception {
        return ((HandlerExecution) executor).handle(request, response);
    }
}
