package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import core.annotation.web.RequestMethod;

public class HandlerKey {

    static {
        var pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
        PATH_PATTERN_PARSER = pathPatternParser;
    }

    private static final PathPatternParser PATH_PATTERN_PARSER;

    private String url;
    private RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HandlerKey other = (HandlerKey)obj;
        if (requestMethod != other.requestMethod)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    public boolean matches(HandlerKey other) {
        return this.requestMethod == other.requestMethod
            && supportPath(other);
    }

    private boolean supportPath(HandlerKey handlerKey) {
        var parse = PATH_PATTERN_PARSER.parse(url);
        if (!parse.hasPatternSyntax()) {
            return false;
        }

        var pathContainer = PathContainer.parsePath(handlerKey.url);
        return parse.matches(pathContainer);
    }
}
