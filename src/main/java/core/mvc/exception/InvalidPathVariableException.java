package core.mvc.exception;

public class InvalidPathVariableException extends RuntimeException {

    private static String MESSAGE = "Path Argument 파싱에 실패했습니다. : pattern(%s), path(%s), key(%s)";

    public InvalidPathVariableException(String pattern, String path, String key) {
        super(String.format(MESSAGE, pattern, path, key));
    }
}
