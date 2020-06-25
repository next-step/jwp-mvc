package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class Helpers {
    private final static List<HandlerMethodHelper> METHOD_HELPERS = new ArrayList<>();

    static {
        METHOD_HELPERS.add(new OriginalParameterBinding());
        METHOD_HELPERS.add(new AnnotationParameterHelper());
        METHOD_HELPERS.add(new ObjectParameterHelper());
    }

    public static Object executeHelper(ParameterInfo parameterInfo, HttpServletRequest request) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Optional<HandlerMethodHelper> handlerMethodHelperOptional = METHOD_HELPERS.stream()
                .filter(methodHelper -> methodHelper.support(parameterInfo))
                .findFirst();

        HandlerMethodHelper handlerMethodHelper = handlerMethodHelperOptional.get();
        return handlerMethodHelper.bindingProcess(parameterInfo, request);
    }
}
