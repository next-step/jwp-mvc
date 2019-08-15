package core.mvc;

/**
 * @author : yusik
 * @date : 2019-08-15
 */
public interface ViewResolver {
    View resolveViewName(String viewName);
}
