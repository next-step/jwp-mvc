package core.mvc.tobe.helper;

import core.mvc.tobe.ParameterInfo;
import core.mvc.tobe.helper.AnnotationParameterHelper;
import core.mvc.tobe.helper.HandlerMethodHelper;
import core.mvc.tobe.helper.ObjectParameterHelper;
import core.mvc.tobe.helper.OriginalParameterBinding;

import javax.servlet.http.HttpServletRequest;
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

    public static Object executeHelper(ParameterInfo parameterInfo, HttpServletRequest request) {
        Optional<HandlerMethodHelper> handlerMethodHelperOptional = METHOD_HELPERS.stream()
                .filter(methodHelper -> methodHelper.support(parameterInfo))
                .findFirst();

        HandlerMethodHelper handlerMethodHelper = handlerMethodHelperOptional.get();
        return handlerMethodHelper.bindingProcess(parameterInfo, request);
    }
}
