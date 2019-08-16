package core.mvc;

public class JspView implements View {
    private String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getViewName() {
        return this.viewName;
    }
}
