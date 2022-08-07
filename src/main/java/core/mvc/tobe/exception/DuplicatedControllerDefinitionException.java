package core.mvc.tobe.exception;

public class DuplicatedControllerDefinitionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "중복된 컨트롤러 인스턴스가 발견되었습니다: %s";

    public DuplicatedControllerDefinitionException(String message) {
        super(String.format(DEFAULT_MESSAGE, message));
    }

}
