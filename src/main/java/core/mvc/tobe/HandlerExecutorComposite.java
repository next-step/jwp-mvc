package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author KingCjy
 */
public class HandlerExecutorComposite implements HandlerExecutor {

    private Set<HandlerExecutor> handlerExecutors = new LinkedHashSet<>();

    public HandlerExecutorComposite(HandlerExecutor ... handlerExecutors) {
        this.handlerExecutors.addAll(Arrays.asList(handlerExecutors));
    }

    @Override
    public boolean supportHandler(Object object) {
        return getHandlerExecutor(object).isPresent();
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        Optional<HandlerExecutor> handlerExecutorOptional = getHandlerExecutor(object);

        if(handlerExecutorOptional.isPresent()) {
            return handlerExecutorOptional.get().execute(request, response, object);
        }

        return null;
    }

    public Optional<HandlerExecutor> getHandlerExecutor(Object object) {
        return this.handlerExecutors.stream()
                .filter(handlerExecutor -> handlerExecutor.supportHandler(object))
                .findAny();
    }
}
