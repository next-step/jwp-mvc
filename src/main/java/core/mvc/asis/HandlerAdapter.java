package core.mvc.asis;


import core.mvc.ModelAndView;
import core.mvc.exception.NoHandlerFoundException;
import core.mvc.tobe.HandlerExecution;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.function.Predicate;

public enum HandlerAdapter {

    CONTROLLER((instance -> instance instanceof Controller)) {
        @Override
        ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception{
            String viewName = ((Controller) handler).execute(req, resp);
            return new ModelAndView(viewName);
        }
    },
    HANDLER_EXECUTION((instance -> instance instanceof HandlerExecution)) {
        @Override
        ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
            return ((HandlerExecution) handler).handle(req, resp);
        }
    };

    private final Predicate<Object> isKindOf;

    HandlerAdapter(Predicate<Object> isKindOf) {
        this.isKindOf = isKindOf;
    }

    abstract ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public static HandlerAdapter of(Object instance) throws ServletException {
        return Arrays.stream(HandlerAdapter.values())
                .filter(handlerAdapter -> handlerAdapter.isKindOf.test(instance))
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException());
    }
}
