package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.view.ViewGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class LegacyMappingAdapter implements HandlerMapping {

    private RequestMapping requestMapping = new RequestMapping();

    @Override
    public void initialize() {
        requestMapping.initMapping();
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return Objects.nonNull(requestMapping.findController(request.getRequestURI()));
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = requestMapping.findController(request.getRequestURI());

        String viewName = controller.execute(request, response);
        return new ModelAndView(ViewGenerator.of(viewName));
    }
}
