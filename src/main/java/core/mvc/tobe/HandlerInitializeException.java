package core.mvc.tobe;

class HandlerInitializeException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Handler 초기화에 실패하였습니다.";

    HandlerInitializeException(final Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
