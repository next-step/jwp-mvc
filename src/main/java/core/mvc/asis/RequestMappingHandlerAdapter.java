package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private RequestMapping requestMapping;

    public RequestMappingHandlerAdapter(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
        requestMapping.initMapping();
    }

    @Override
    public boolean supports(HttpServletRequest req) {
        return requestMapping.containsController(req.getRequestURI());
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Controller controller = requestMapping.findController(req.getRequestURI());
        String viewName = controller.execute(req, resp);
        return new ModelAndView(viewName);
    }
}
