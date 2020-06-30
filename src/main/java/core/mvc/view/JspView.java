package core.mvc.view;

import core.utils.Args;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class JspView implements View {

    private static final String ILLEGAL_JSP_VIEW_NAME = "유효하지 않은 view name 입니다.: ";
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = validate(viewName);
    }

    private String validate(String viewName) {
        String name = Args.notNull(viewName, ILLEGAL_JSP_VIEW_NAME + viewName);
        Args.check(name.endsWith(".jsp"), ILLEGAL_JSP_VIEW_NAME + viewName);
        return name;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);
        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
}
