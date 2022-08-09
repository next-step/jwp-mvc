package core.mvc.tobe;

import java.util.NoSuchElementException;

public class NoExistsHandlerException extends NoSuchElementException {
    public NoExistsHandlerException(String s) {
        super(s);
    }
}
