package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created By kjs4395 on 2020-06-18
 */
public class ControllerExecutor {

    public static Object findExecutor(List<HandlerMapping> mappings, HttpServletRequest request) {

        Optional<Object> executor = mappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();

        return executor.get();
    }
}
