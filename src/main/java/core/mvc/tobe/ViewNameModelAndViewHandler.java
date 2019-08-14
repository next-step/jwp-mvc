package core.mvc.tobe;

import core.mvc.ModelAndView;

public class ViewNameModelAndViewHandler implements ModelAndViewHandler {
    @Override
    public ModelAndView handle(Object result) {
        return new ModelAndView((String) result);
    }
}
