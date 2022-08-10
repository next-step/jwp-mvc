package core.mvc.tobe.handler.adapter;

import java.util.NoSuchElementException;

public class NoExistsAdapterException extends NoSuchElementException {
    public NoExistsAdapterException(String s) {
        super(s);
    }
}
