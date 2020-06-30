package core.mvc.view;

import core.utils.Args;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/23 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class RedirectView implements View {

    private static final String ILLEGAL_REDIRECT_VIEW_NAME = "유효하지 않은 view name 입니다.: ";
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public RedirectView(String viewName) {
        this.viewName = validate(viewName);
    }

    private String validate(String viewName) {
        String name = Args.notNull(viewName, ILLEGAL_REDIRECT_VIEW_NAME + viewName);
        Args.check(name.startsWith(DEFAULT_REDIRECT_PREFIX), ILLEGAL_REDIRECT_VIEW_NAME + viewName);
        return name;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
    }
}
