package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandlerKeyGenerator {

    private static final String EMPTY_VALUE = "";
    private static final RequestMethod[] EMPTY_REQUEST_METHOD = {};

    private HandlerKeyGenerator() {
        throw new AssertionError();
    }

    public static List<HandlerKey> generate(final RequestMapping controllerAnnotation, final RequestMapping methodAnnotation) {

        final String controllerValue = getControllerValue(controllerAnnotation);
        final Set<RequestMethod> requestMethods = mergeRequestMethods(controllerAnnotation, methodAnnotation);

        return requestMethods.stream()
            .map(it -> new HandlerKey(controllerValue + methodAnnotation.value(), it))
            .collect(Collectors.toList());
    }

    private static String getControllerValue(final RequestMapping controllerAnnotation) {
        if (controllerAnnotation == null) {
            return EMPTY_VALUE;
        }

        return controllerAnnotation.value();
    }

    private static Set<RequestMethod> mergeRequestMethods(final RequestMapping controllerAnnotation, final RequestMapping methodAnnotation) {
        final RequestMethod[] controllerRequestMethods = getRequestMethods(controllerAnnotation);
        final RequestMethod[] methodRequestMethods = getRequestMethods(methodAnnotation);

        if (hasMethodsOnlyOne(controllerRequestMethods, methodRequestMethods)) {
            return Arrays.stream(controllerRequestMethods)
                .collect(Collectors.toSet());
        }

        if (hasMethodsOnlyOne(methodRequestMethods, controllerRequestMethods)) {
            return Arrays.stream(methodRequestMethods)
                .collect(Collectors.toSet());
        }

        return Stream.of(controllerRequestMethods, methodRequestMethods)
            .flatMap(Arrays::stream)
            .collect(Collectors.toSet());
    }

    private static RequestMethod[] getRequestMethods(final RequestMapping annotation) {
        if (annotation == null) {
            return EMPTY_REQUEST_METHOD;
        }

        return getRequestMethods(annotation.method());
    }

    private static RequestMethod[] getRequestMethods(final RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    private static boolean hasMethodsOnlyOne(final RequestMethod[] target, final RequestMethod[] other) {
        if (target.length == 0) {
            return false;
        }

        return Arrays.equals(other, RequestMethod.values());
    }

}
