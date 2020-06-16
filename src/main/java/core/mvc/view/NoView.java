package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class NoView implements View {

    public static NoView INSTANCE = new NoView();

    private NoView() {
        // prevent instantiation
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // no-op
    }
}
