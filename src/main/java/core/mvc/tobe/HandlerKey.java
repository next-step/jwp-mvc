package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HandlerKey {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    private final String url;
    private final PathPattern pathPattern;
    private final RequestMethod requestMethod;

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        if (Objects.isNull(url) || Objects.isNull(requestMethod)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz there is null argument");
        }

        this.url = url;
        this.pathPattern = PATH_PATTERN_PARSER.parse(url);
        this.requestMethod = requestMethod;
    }

    public boolean isSupport(HttpServletRequest request) {
        HandlerKey requestKey = HandlerKey.from(request);
        PathContainer pathContainer = toPathContainer(request.getRequestURI());

        return requestMethod == requestKey.requestMethod && pathPattern.matches(pathContainer);
    }

    public static HandlerKey from(final HttpServletRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz HttpRequest is null");
        }

        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase()));
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }

    public Map<String, String> parseRequestParam(String requestURI) {
        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(requestURI));

        if (pathMatchInfo == null) {
            return Collections.emptyMap();
        }

        return pathMatchInfo.getUriVariables();
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    /*
     * url과 메서드가 같은 경우일 때만 키가 같다. 따라서 path Pattern이 들어가는 순간 애매해짐
     * path pattern 매칭을 위한 함수를 두고 일치하는 것들을 리턴하는 것이 나음
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) &&
                requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
