package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public enum DummyView implements View {
    INSTANCE;

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        //do nothing
    }
}
