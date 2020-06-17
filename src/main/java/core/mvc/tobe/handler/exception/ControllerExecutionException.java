package core.mvc.tobe.handler.exception;

public class ControllerExecutionException extends RuntimeException{
    public ControllerExecutionException(Throwable cause) {
        super("Failed to execute controller in HandlerExecution!", cause);
    }
}
