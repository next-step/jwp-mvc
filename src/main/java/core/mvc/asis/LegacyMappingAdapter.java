package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.tobe.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyMappingAdapter implements HandlerMapping {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private RequestMapping requestMapping = new RequestMapping();

    @Override
    public void initialize() {
        requestMapping.initMapping();
    }

    @Override
    public boolean isExists(HttpServletRequest request) {
        return null != requestMapping.findController(request.getRequestURI());
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = requestMapping.findController(request.getRequestURI());

        String viewName = controller.execute(request, response);

        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            String redirectViewName = viewName.substring(DEFAULT_REDIRECT_PREFIX.length());
            return new ModelAndView(new RedirectView(redirectViewName));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
