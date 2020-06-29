package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class ParameterUtils {

    public static Object decideParameter(String parameter, Class<?> parameterType) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameter);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameter);
        }

        return parameter;
    }

    public static PathPattern parsePath(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
