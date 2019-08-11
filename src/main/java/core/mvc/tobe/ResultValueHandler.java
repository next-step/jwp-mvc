package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public enum ResultValueHandler {

    MODEL_AND_VIEW {
        @Override
        public boolean support(Object result) {
            return result instanceof ModelAndView;
        }

        @Override
        public void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception {
            ModelAndView mav = (ModelAndView) result;
            View view = mav.getView();
            view.render(mav.getModel(), request, response);
        }
    },

    STRING {
        @Override
        public boolean support(Object result) {
            return result instanceof String;
        }

        @Override
        public void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception {
            String viewName = (String) result;
            JspView jspView = new JspView(viewName);
            jspView.render(new HashMap<>(), request, response);
        }
    };

    public static void execute(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (ResultValueHandler handler : values()) {
            if (handler.support(result)) {
                handler.handle(result, request, response);
            }
        }
    }

    public abstract boolean support(Object result);

    public abstract void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
