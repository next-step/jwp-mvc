package core.mvc.asis;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import core.mvc.View;

public class JspView implements View {

    static {
        TemplateLoader loader = new FileTemplateLoader(System.getProperty("user.dir") + "/webapp");
        loader.setPrefix("/views");
        loader.setSuffix(".jsp");
        HANDLEBARS = new Handlebars(loader);
    }

    private static final Handlebars HANDLEBARS;

    private final String templatePath;

    public JspView(String path) {
        this.templatePath = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws
        Exception {
        var template = HANDLEBARS.compile(templatePath);
        var html = template.apply(model);

        var writer = response.getWriter();
        writer.write(html);
        writer.flush();
    }
}
