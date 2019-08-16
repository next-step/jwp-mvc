package core.mvc;

/**
 * Created by hspark on 2019-08-16.
 */
public class HandlerNotFoundException extends RuntimeException{
    private static final String MESSAGE = "RequestMappingHandler Not Found, URL : %s";
    public HandlerNotFoundException(String url) {
        super(String.format(MESSAGE, url));
    }
}
