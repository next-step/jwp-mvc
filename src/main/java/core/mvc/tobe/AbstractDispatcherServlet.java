package core.mvc.tobe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Deprecated
public abstract class AbstractDispatcherServlet extends HttpServlet {

    @Override
    public abstract void init() throws ServletException;

    @Override
    protected abstract void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
