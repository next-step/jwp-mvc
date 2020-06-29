package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.argumentresolver.ArgumentResolvers;
import core.mvc.argumentresolver.MethodParameter;
import core.mvc.tobe.HandlerMappings;
import core.mvc.tobe.HandlerMethod;
import core.mvc.view.View;
import core.mvc.view.ViewResolvers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ArgumentResolvers argumentResolvers = new ArgumentResolvers();
    private final HandlerMappings handlerMappings = new HandlerMappings();
    private final ViewResolvers viewResolvers = new ViewResolvers();

    @Override
    public void init() {
        argumentResolvers.init();
        handlerMappings.init();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        HandlerMethod handlerMethod = handlerMappings.getHandlerMethod(request);
        if (handlerMethod == null) {
            log.error("404 Not Found : URI = {}", requestUri);
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        try {
            MethodParameter[] methodParameters = getMethodParametersFrom(handlerMethod);
            Object[] arguments = argumentResolvers.resolve(methodParameters, request, response);

            ModelAndView modelAndView = handlerMethod.handle(arguments);

            View view = resolveView(modelAndView);
            if (view == null) {
                throw new IllegalStateException("View Does Not Exist.");
            }

            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private MethodParameter[] getMethodParametersFrom(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();

        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < methodParameters.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            String parameterName = parameterNames[i];

            methodParameters[i] = new MethodParameter(parameterType, parameterName);
        }

        return methodParameters;
    }

    private View resolveView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        return (viewName != null) ? viewResolvers.resolve(viewName) : modelAndView.getView();
    }
}
