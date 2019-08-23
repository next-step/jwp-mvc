package core.mvc.tobe;

/**
 * Created by hspark on 2019-08-18.
 */
public class ControllerInstantiateFailedException extends RuntimeException {
    public ControllerInstantiateFailedException(Class<?> controllerClass) {
        super("Failed Instantiate, controller class : " + controllerClass.toString());
    }
}
