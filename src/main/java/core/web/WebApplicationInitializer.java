package core.web;

import core.mvc.asis.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebApplicationInitializer implements ApplicationInitializer {

    private static final String DEFAULT_DISPATCHER_SERVLET_NAME = "dispatcherServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet(DEFAULT_DISPATCHER_SERVLET_NAME, new DispatcherServlet());
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }
}
