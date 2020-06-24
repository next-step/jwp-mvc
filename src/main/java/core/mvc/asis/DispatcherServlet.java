package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMappings;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final HandlerMappings handlerMappings = new HandlerMappings();

    @Override
    public void init() {
        handlerMappings.init();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        HandlerExecution handler = handlerMappings.getHandler(request);
        if (handler == null) {
            log.error("404 Not Found : URI = {}", requestUri);
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        try {
            ModelAndView modelAndView = handler.handle(request, response);
            modelAndView.render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
