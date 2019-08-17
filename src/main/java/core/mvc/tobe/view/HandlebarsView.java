package core.mvc.tobe.view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class HandlebarsView implements View {

    String viewName;

    public HandlebarsView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String content = getHandlebarsContent(model, request);
        final Object users = model.get("users");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        response.setContentLength(content.getBytes().length);
        response.getWriter().print(content);
    }

    private String getHandlebarsContent(Map<String, ?> model, HttpServletRequest request) throws IOException {
        Handlebars handlebars = new Handlebars(new ServletContextTemplateLoader(request.getServletContext()));
        Template template = handlebars.compile(viewName);
        return template.apply(model);
    }
}
