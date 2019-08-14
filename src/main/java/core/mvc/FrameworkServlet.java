package core.mvc;

import javax.servlet.http.HttpServlet;

public abstract class FrameworkServlet extends HttpServlet {

    @Override
    public void init() {
        initStrategies(getApplicationContext());
    }

    protected abstract void initStrategies(ApplicationContext context);

    protected ApplicationContext getApplicationContext() {
        if (getServletConfig() != null) {
            return (ApplicationContext) getServletConfig().getServletContext().getAttribute(ApplicationContext.class.getName());
        }

        throw new IllegalStateException("servlet config must not null");
    }

}
