package core.mvc.tobe;


import javax.servlet.http.HttpServletRequest;

public interface ArgumentResolver {
    boolean equalsTo(Class parameterType);

    Object getParameterValue(final HttpServletRequest request, final Class parameterType, String parameterName) throws Exception;
}
