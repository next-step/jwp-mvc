package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerKeyGenerator {

    private static final String DEFAULT_VALUE = "";

    private HandlerKeyGenerator() {
        throw new AssertionError();
    }

    public static List<HandlerKey> generate(final RequestMapping controllerAnnotation, final RequestMapping methodAnnotation) {

        final String controllerValue = getControllerValue(controllerAnnotation);

        return Arrays.stream(RequestMethod.values())
            .map(it -> new HandlerKey(controllerValue + methodAnnotation.value(), it))
            .collect(Collectors.toList());
    }

    private static String getControllerValue(final RequestMapping controllerAnnotation) {
        if (controllerAnnotation == null) {
            return DEFAULT_VALUE;
        }
        return controllerAnnotation.value();
    }
}
