package core.exception;

import lombok.Getter;

@Getter
public enum CoreExceptionStatus {
    CLASS_NEW_INSTANCE_FAIL("class new instance fail");

    private String message;

    CoreExceptionStatus(String message) {
        this.message = message;
    }
}
