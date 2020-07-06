package core.mvc.tobe.argumentresolver.exception;

public class TargetBeanCreateException extends RuntimeException{
    public TargetBeanCreateException() {
        super("Exception occurred during creating a bean using a default constructor by Method Parameter!");
    }
}
