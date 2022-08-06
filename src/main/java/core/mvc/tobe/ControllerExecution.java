package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerExecution implements HandlerExecutable {

    private final Controller controller;

    public ControllerExecution(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = controller.execute(request, response);
        final ModelAndView modelAndView = new ModelAndView(new JspView(viewName));

        copyRequestAttributesIntoModelObjects(request, modelAndView);

        return modelAndView;
    }

    private static void copyRequestAttributesIntoModelObjects(final HttpServletRequest request, final ModelAndView modelAndView) {
        final Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            final String attributeName = attributeNames.nextElement();
            final Object attributeValue = request.getAttribute(attributeName);
            copyAttribute(modelAndView, attributeName, attributeValue);
        }
    }

    private static void copyAttribute(final ModelAndView modelAndView, final String attributeName, final Object attributeValue) {
        if (Objects.nonNull(attributeValue)) {
            modelAndView.addObject(attributeName, attributeValue);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ControllerExecution that = (ControllerExecution) o;
        return controller.getClass().equals(that.controller.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(controller);
    }
}
