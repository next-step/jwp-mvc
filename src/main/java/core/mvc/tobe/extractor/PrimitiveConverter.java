package core.mvc.tobe.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

final class PrimitiveConverter {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveConverter.class);

    private PrimitiveConverter() { }

    static Object convert(final Class<?> clazz,
                          final String value) {
        System.out.println("@@@@@@@@@");
        System.out.println(clazz);
        System.out.println(value);

        if (Objects.isNull(value)) {
            return null;
        }
        if (Integer.class == clazz || Integer.TYPE == clazz) {
            return Integer.parseInt(value);
        }
        if (Long.class == clazz || Long.TYPE == clazz) {
            return Long.parseLong(value);
        }

        // TODO: add type

        log.debug("Not support [type={}, value={}]", clazz, value);
        return value;
    }
}
