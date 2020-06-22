package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import java.util.Objects;
import javassist.NotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerExecutionViewResolver implements ViewResolver {

    private static final String ILLEGAL_HANDLER = "해당 요청을 처리할 handler가 없습니다: ";

    @Override
    public ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (Objects.isNull(handler)) {
            throw new NotFoundException(ILLEGAL_HANDLER + request.getRequestURI());
        }
        return ((HandlerExecution) handler).handle(request, response);
    }
}
