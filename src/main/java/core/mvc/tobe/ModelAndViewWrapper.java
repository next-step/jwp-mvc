package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.utils.ReflectionUtils;

public class ModelAndViewWrapper {
    public static ModelAndView wrap(String viewName, Class<? extends View> viewClass) throws ReflectiveOperationException {
        View view = (View) ReflectionUtils.getInstance(viewClass, viewName);
        return new ModelAndView(view);
    }
}
