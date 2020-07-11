package core.mvc.tobe;

import core.mvc.Controller;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AnnotationController implements Controller {

    private final Class clazz;
    private final Method method;

    AnnotationController(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView results = (ModelAndView) method.invoke(clazz.newInstance(), request, response);
        return results.getViewName();
    }

}
