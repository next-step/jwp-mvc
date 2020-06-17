package core.mvc.view;

import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ViewResolverFactory {

    // TODO: 매번 생성하는게 옳을까? stateless한 ViewResolver를 만든다면 해결되지 않을까? 고민은 일단 만들고나서.. =_=
    public static ViewResolver of(Object handler, HttpServletRequest req, HttpServletResponse resp) {

        if (handler instanceof Controller) {
            return new LegacyControllerViewResolver((Controller) handler);
        }

        if (handler instanceof HandlerExecution) {
            return new HandlerExecutionViewResolver((HandlerExecution) handler);
        }

        throw new IllegalArgumentException("suitable candidate not found.");
    }
}
