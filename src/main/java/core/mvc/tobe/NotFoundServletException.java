package core.mvc.tobe;

public class NotFoundServletException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "해당하는 서블릿이 없습니다.";

    public NotFoundServletException() {
        super(ERROR_MESSAGE);
    }
}
