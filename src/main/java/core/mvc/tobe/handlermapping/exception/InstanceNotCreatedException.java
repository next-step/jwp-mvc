package core.mvc.tobe.handlermapping.exception;

public class InstanceNotCreatedException extends RuntimeException{
    public InstanceNotCreatedException(Throwable cause) {
        super("Failed to create an instance using reflection!", cause);
    }

    public InstanceNotCreatedException() {
        super();
    }
}
