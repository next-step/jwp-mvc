package core.mvc.tobe;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;

public interface HandlerExecutable {

    boolean executable();

    ModelAndView handle(Object... arguments) throws Exception;

    Method getMethod();
}
