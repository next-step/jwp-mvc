package core.mvc.support.exception;

public class MissingPathPatternException extends RuntimeException {

    public MissingPathPatternException(String parameterName) {
        super("url 에서 '" + parameterName + "'를 가져올 수 없음");
    }

}
