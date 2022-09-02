package core.mvc.exception;

public enum ErrorMessage {
    INVALID_PARAMETER_ANNOTAION("%s 어노테이션을 찾지 못했습니다."),
    INVALID_PATH_VARIABLE("Path Argument 파싱에 실패했습니다. : pattern(%s), path(%s), key(%s)"),
    METHOD_ARGUMENT_TYPE_NOT_SUPORT("%s 를 %s 로 변환하는 것은 지원되지 않습니다."),
    NOT_MATCH_HANDLER_KEY("%s url을 찾지 못하였습니다."),
    METHOD_RESOLVER_NOT_SUPPORT("%s 를 에 해당되는 resolver 가 존재하지 않습니다."),
    NOT_PRESENT_REQUEST_MAPPING("%s 메서드에 RequestMapping 어노테이션이 붙어있지 않습니다..");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
