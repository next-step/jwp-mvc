package core.mvc.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {

    private final String redirectPath;

    public RedirectView(String redirectPath) {
        Assert.notNull(redirectPath, "redirectPath가 null이어선 안됩니다.");
        this.redirectPath = redirectPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        render(response);
    }

    private void render(HttpServletResponse response) throws IOException {
        response.sendRedirect(redirectPath);
    }
}
