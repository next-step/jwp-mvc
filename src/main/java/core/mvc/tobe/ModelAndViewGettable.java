package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public interface ModelAndViewGettable {

    default ModelAndView getModelAndView(HttpServletRequest req, String viewName) {
        ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
        addModelObjects(req, modelAndView);
        return modelAndView;
    }

    default void addModelObjects(HttpServletRequest req, ModelAndView modelAndView) {
        Enumeration<String> attributeNames = req.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            modelAndView.addObject(name, req.getAttribute(name));
        }
    }
}
