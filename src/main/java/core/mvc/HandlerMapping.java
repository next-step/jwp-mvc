package core.mvc;

import core.mvc.tobe.HandlerKey;

public interface HandlerMapping {

    void init();
    Object getHandler(HandlerKey handlerKey);

}
