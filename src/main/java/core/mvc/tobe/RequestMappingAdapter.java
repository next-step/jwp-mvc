package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.requestAdapter.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class RequestMappingAdapter implements HandlerAdapter {
    @Override
    public boolean isSupport(Object executor) {
        return executor instanceof Controller;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object executor) throws Exception {
        return  new ModelAndView(((Controller) executor).execute(request, response));
    }
}
