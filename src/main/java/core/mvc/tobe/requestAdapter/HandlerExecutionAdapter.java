package core.mvc.tobe.requestAdapter;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class HandlerExecutionAdapter implements HandlerAdapter {
    @Override
    public boolean isSupport(Object executor) {
        return executor instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object executor) throws Exception {
        return ((HandlerExecution) executor).handle(request, response);
    }
}
