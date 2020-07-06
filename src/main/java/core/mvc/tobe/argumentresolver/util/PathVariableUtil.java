package core.mvc.tobe.argumentresolver.util;

import core.mvc.tobe.argumentresolver.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

public class PathVariableUtil {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    public static Map<String, String> getUriVariables(String path, String requestURI) {
        return parse(path).matchAndExtract(toPathContainer(requestURI))
                .getUriVariables();
    }

    public static boolean hasPathVariable(MethodParameter methodParameter){
        return PATH_PATTERN_PARSER.parse(methodParameter.getPath())
                .matches(toPathContainer(methodParameter.getRequestURI()));
    }

    private static PathPattern parse(String path) {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
        return PATH_PATTERN_PARSER.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
