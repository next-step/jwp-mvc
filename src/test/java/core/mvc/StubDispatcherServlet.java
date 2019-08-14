package core.mvc;

import org.springframework.mock.web.MockServletContext;

public class StubDispatcherServlet extends DispatcherServlet {

    @Override
    protected ApplicationContext getApplicationContext() {
        ApplicationContext context = new ApplicationContext();
        context.init(new MockServletContext());

        return context;
    }
}
