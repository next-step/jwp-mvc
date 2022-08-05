package core.mvc.tobe;

import core.annotation.web.Controller;

public class HandlerName {

    private HandlerName() {
        throw new AssertionError();
    }

    public static String generate(final Class<?> handler) {
        final Controller controller = handler.getAnnotation(Controller.class);
        validateControllerAnnotation(controller, handler.getName());

        final String annotationValue = controller.value();
        if (hasAnnotationValue(annotationValue)) {
            return annotationValue;
        }

        return toLowerCamelCase(handler.getSimpleName());
    }

    private static boolean hasAnnotationValue(final String annotationValue) {
        return annotationValue != null && !annotationValue.isBlank();
    }

    private static void validateControllerAnnotation(final Controller controller, final String name) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller 애너테이션이 없습니다 : " + name);
        }
    }

    private static String toLowerCamelCase(final String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }


}
