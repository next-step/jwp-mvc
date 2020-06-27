package core.mvc.tobe;

import core.mvc.ModelAndView;

public interface HandlerCommand {
    ModelAndView execute(Object handler) throws Exception;
}
