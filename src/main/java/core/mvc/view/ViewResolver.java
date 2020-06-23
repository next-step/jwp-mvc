package core.mvc.view;

/**
 * View를 결정하는 역할을 하는 인터페이스
 */
public interface ViewResolver {
    boolean supports(Class<?> arg0);

    View resolveViewName(String viewName);
}
