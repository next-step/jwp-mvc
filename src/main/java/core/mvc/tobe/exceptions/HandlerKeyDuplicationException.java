package core.mvc.tobe.exceptions;

import core.mvc.tobe.HandlerKey;

/**
 * Created by hspark on 2019-08-14.
 */
public class HandlerKeyDuplicationException extends RuntimeException {
    public HandlerKeyDuplicationException(HandlerKey handlerKey) {
        super("duplicate handler Key, " + handlerKey.toString());
    }
}
