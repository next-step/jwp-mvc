package core.annotation.web;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, ALL;

    public static RequestMethod[] all() {
        return new RequestMethod[]{GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE};
    }
}
