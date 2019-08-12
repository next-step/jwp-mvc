package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public enum ResultValueHandler {

    MODEL_AND_VIEW(ModelAndView.class) {
        @Override
        public void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception {
            ModelAndView mav = (ModelAndView) result;
            View view = mav.getView();
            view.render(mav.getModel(), request, response);
        }
    },

    STRING(String.class) {
        @Override
        public void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception {
            String viewName = (String) result;
            JspView jspView = new JspView(viewName);
            jspView.render(new HashMap<>(), request, response);
        }
    };

    ResultValueHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    private static Map<Class<?>, ResultValueHandler> resultValueHandlers = new HashMap<>();
    
    static {
        for (ResultValueHandler handler : ResultValueHandler.values()) {
            resultValueHandlers.put(handler.getClazz(), handler);
        }
    }

    public static void execute(Object result, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resultValueHandlers.get(result.getClass())
                .handle(result, req, resp);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    private Class<?> clazz;

    public abstract void handle(Object result, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
