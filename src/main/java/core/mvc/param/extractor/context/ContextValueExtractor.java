package core.mvc.param.extractor.context;

import core.mvc.param.Parameter;
import core.mvc.param.extractor.ValueExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContextValueExtractor implements ValueExtractor {

    @Override
    public Object extract(final Parameter parameter, final HttpServletRequest request) {
        Context context = Context.of(parameter.getTypeClass());

        if (context == null) {
            return null;
        }

        return context.convert(request);
    }

    private enum Context {
        REQUEST(HttpServletRequest.class, request -> request),
        SESSION(HttpSession.class, HttpServletRequest::getSession);

        private final Class<?> type;
        private final Function<HttpServletRequest, Object> converter;

        private static final Map<Class<?>, Context> CONTEXTS = Arrays.stream(Context.values())
                .collect(Collectors.toMap(Context::getType, Function.identity()));

        private Class<?> getType() {
            return type;
        }

        Context(final Class<?> type, Function<HttpServletRequest, Object> converter) {
            this.type = type;
            this.converter = converter;
        }

        public static Context of(Class<?> type) {
            return CONTEXTS.get(type);
        }

        public Object convert(HttpServletRequest request) {
            return converter.apply(request);
        }
    }
}
