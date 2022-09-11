package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controller;
    private Method m;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.m = method;
    }

//    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return null;
//    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<Controller> clazz = (Class<Controller>) controller.getClass();
        m.invoke(clazz.newInstance(), request, response);
    }
}
