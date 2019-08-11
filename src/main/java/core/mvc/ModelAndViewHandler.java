package core.mvc;

public class ModelAndViewHandler {
    public static ModelAndView createModelAndView(String urlPath) {
        return new ModelAndView(new ResultView(urlPath));
    }
}
