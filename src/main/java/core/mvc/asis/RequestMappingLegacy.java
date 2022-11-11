package core.mvc.asis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingLegacy {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, ControllerLegacy> mappings = new HashMap<>();

    void initMapping() {
    }

    public ControllerLegacy findController(String url) {
        return mappings.get(url);
    }

    void put(String url, ControllerLegacy controllerLegacy) {
        mappings.put(url, controllerLegacy);
    }
}
