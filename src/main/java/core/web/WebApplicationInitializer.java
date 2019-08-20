package core.web;

import core.mvc.DispatcherServlet;
import core.web.context.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebApplicationInitializer implements ApplicationInitializer {

    private static final String DEFAULT_DISPATCHER_SERVLET_NAME = "dispatcherServlet";
    public static final String DEFAULT_CONTROLLER_PACKAGE = "next.controller";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet(DEFAULT_DISPATCHER_SERVLET_NAME, new DispatcherServlet(new ApplicationContext(DEFAULT_CONTROLLER_PACKAGE)));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }
}
