package core.mvc.exception;

import static core.mvc.exception.ErrorMessage.INVALID_PATH_VARIABLE;

public class InvalidPathVariableException extends RuntimeException {
    public InvalidPathVariableException(String pattern, String path, String key) {
        super(String.format(INVALID_PATH_VARIABLE.getMessage(), pattern, path, key));
    }

}
