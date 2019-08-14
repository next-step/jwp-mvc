package core.mvc.asis;

import core.mvc.Handler;
import core.mvc.ModelAndView;
import core.mvc.exepction.HandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyHandlerAdapter implements Handler {
    private static Logger logger = LoggerFactory.getLogger(LegacyHandlerAdapter.class);

    private final LegacyHandlerMapping handlerMapping;

    public LegacyHandlerAdapter(LegacyHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        Controller handler = getHandler(request);

        if (handler == null) {
            return null;
        }
        
        try {
            return new ModelAndView(handler.execute(request, response));
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    private Controller getHandler(HttpServletRequest request) {
        return handlerMapping.getHandler(request);
    }
}
