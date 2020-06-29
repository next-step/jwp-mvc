package core.mvc.tobe;

import core.mvc.tobe.PrimitiveType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created By kjs4395 on 2020-06-24
 */
public class PrimitiveTypeUtil {

    public static List<TypeChecker> typeCheckers = new ArrayList<>();

    static {
        typeCheckers.add(new BooleanTypeChecker());
        typeCheckers.add(new ByteTypeChecker());
        typeCheckers.add(new DoubleTypeChecker());
        typeCheckers.add(new FloatTypeChecker());
        typeCheckers.add(new IntegerTypeChecker());
        typeCheckers.add(new LongTypeChecker());
        typeCheckers.add(new ShortTypeChecker());
    }

    public static Object getValue(Class<?> clazz, String value) {
        Optional<TypeChecker> typeCheckerOptional = typeCheckers.stream()
                .filter(typeChecker -> typeChecker.isSupportType(clazz))
                .findFirst();

        if (typeCheckerOptional.isPresent()) {
            return typeCheckerOptional.get().parseType(value);
        }

        return value;
    }
}
