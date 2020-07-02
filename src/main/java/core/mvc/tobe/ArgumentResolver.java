package core.mvc.tobe;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {
    boolean equalsTo(Class parameterType);

    Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final Class parameterType, String parameterName) throws Exception;
}
