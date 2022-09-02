package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.NOT_MATCH_HANDLER_KEY;

public class NotMatchHandlerKeyException extends RuntimeException {
    public NotMatchHandlerKeyException(String url) {
        super(String.format(NOT_MATCH_HANDLER_KEY.getMessage(), url));
    }
}
