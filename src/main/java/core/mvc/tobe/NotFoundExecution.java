package core.mvc.tobe;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;

public class NotFoundExecution implements HandlerExecutable {

    @Override
    public boolean executable() {
        return true;
    }

    @Override
    public ModelAndView handle(Object... arguments) throws Exception {
        return ModelAndView.jsp("/404.jsp");
    }

    @Override
    public Method getMethod() {
        return null;
    }
}
