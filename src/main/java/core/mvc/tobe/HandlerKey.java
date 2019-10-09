package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.tobe.utils.PathUtils;

class HandlerKey {
    private final String url;
    private final RequestMethod requestMethod;

    HandlerKey(final String url,
               final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    boolean match(final String path,
                  final RequestMethod requestMethod) {
        return PathUtils.matches(url, path) && this.requestMethod == requestMethod;
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }
}
