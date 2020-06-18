package core.mvc.tobe.handlermapping.exception;

public class ControllerScanException extends RuntimeException{
    private static final String MESSAGE_CONTROLLER_SCAN
            = "Please check where the base package has any controller with Controller annotation";

    public ControllerScanException(Throwable cause) {
        super(MESSAGE_CONTROLLER_SCAN, cause);
    }
}
