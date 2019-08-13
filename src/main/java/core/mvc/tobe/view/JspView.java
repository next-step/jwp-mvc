package core.mvc.tobe.view;

import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : yusik
 * @date : 2019-08-13
 */
public class JspView implements View {
    public JspView(String viewName) {
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
