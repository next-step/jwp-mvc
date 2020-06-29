package core.mvc.tobe;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;

public class BasicTypeArgumentResolver extends AbstractArgumentResolver {

    public ModelAndView handle(Class clazz, Method method) throws Exception {

//        method.invoke(clazz, objects);
        return null;
    }
}
