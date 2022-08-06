package core.mvc.tobe;

import core.annotation.web.Controller;
import core.mvc.tobe.exception.UnSupportedControllerInstanceException;

public class RequestMappingScanner {

    private RequestMappingScanner() {
        throw new AssertionError();
    }

    public static void getHandlerExecutable(final Class<?> clazz) {
        final Controller annotation = clazz.getAnnotation(Controller.class);
        validateExistenceOfControllerAnnotation(annotation, clazz.getName());

    }

    private static void validateExistenceOfControllerAnnotation(final Controller annotation, final String name) {
        if (annotation == null) {
            throw new UnSupportedControllerInstanceException(name);
        }
    }
}
