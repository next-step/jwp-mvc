package core.mvc.tobe.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By kjs4395 on 2020-06-25
 *
 * todo : DI로 변경 : 어디서 주입하지...?
 */
public class HandlerMethodHelperAdapter {
    public static final List<HandlerMethodHelper> METHOD_HELPERS = new ArrayList<>();

    static {
        METHOD_HELPERS.add(new AnnotationParameterHelper());
        METHOD_HELPERS.add(new ObjectParameterHelper());
        METHOD_HELPERS.add(new OriginalParameterHelper());
    }
}