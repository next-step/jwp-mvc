package core.mvc.tobe.requestAdapter;

import core.mvc.ModelAndView;
import core.mvc.tobe.RequestMappingAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By kjs4395 on 2020-06-25
 */
public class HandlerAdapters {
    private static List<HandlerAdapter> HANDLERS = new ArrayList<>();

    static {
        HANDLERS.add(new RequestMappingAdapter());
        HANDLERS.add(new HandlerExecutionAdapter());
    }

    public static ModelAndView executeHandler(HttpServletRequest req, HttpServletResponse resp, Object executor) throws Exception {
        return HANDLERS.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(executor))
                .findFirst()
                .get()
                .execute(req, resp, executor);
    }
}
