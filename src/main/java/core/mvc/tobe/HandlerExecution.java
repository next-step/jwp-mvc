package core.mvc.tobe;

import core.mvc.ModelAndView;

public interface HandlerExecution {

    ModelAndView handle(Object... args) throws Exception;
}
