package core.mvc.tobe;

/**
 * @author KingCjy
 */
public class PageNotFountException extends RuntimeException {

    private String requestURI;

    public PageNotFountException(String message, String requestURI) {
        super(message);
        this.requestURI = requestURI;
    }

    public String getRequestURI() {
        return requestURI;
    }
}
