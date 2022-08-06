package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandlerKeyGenerator {

    private static final String DEFAULT_VALUE = "";

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

    private static Set<RequestMethod> mergeRequestMethods(final RequestMapping controllerAnnotation, final RequestMapping methodAnnotation) {
        final RequestMethod[] controllerRequestMethods = getRequestMethods(controllerAnnotation);
        final RequestMethod[] methodRequestMethods = getRequestMethods(methodAnnotation);

        return Stream.of(controllerRequestMethods, methodRequestMethods)
            .flatMap(Arrays::stream)
            .collect(Collectors.toSet());
    }

    private static RequestMethod[] getRequestMethods(final RequestMapping annotation) {
        if (annotation == null) {
            return new RequestMethod[]{};
        }

        if (annotation.method().length == 0) {
            return RequestMethod.values();
        }
        return annotation.method();
    }

    private static String getControllerValue(final RequestMapping controllerAnnotation) {
        if (controllerAnnotation == null) {
            return DEFAULT_VALUE;
        }
        return controllerAnnotation.value();
    }
}
