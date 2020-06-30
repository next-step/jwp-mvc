package core.utils.typeresolver;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class PrimitiveTypeResolvers {

    private static final List<TypeResolver> typeResolvers = Lists.newArrayList();

    static {
        typeResolvers.add(new BoolTypeResolver());
        typeResolvers.add(new CharacterTypeResolver());
        typeResolvers.add(new ByteTypeResolver());
        typeResolvers.add(new ShortTypeResolver());
        typeResolvers.add(new IntegerTypeResolver());
        typeResolvers.add(new LongTypeResolver());
        typeResolvers.add(new FloatTypeResolver());
        typeResolvers.add(new DoubleTypeResolver());
        typeResolvers.add(new StringTypeResolver());
    }

    private PrimitiveTypeResolvers() {
    }

    public static Object resolve(String parameter, Class<?> type) {
        TypeResolver typeResolver = getTypeResolver(type);
        return typeResolver.resolve(parameter);
    }

    private static TypeResolver getTypeResolver(Class<?> type) {
        return typeResolvers.stream()
            .filter(typeResolver -> typeResolver.isSupport(type))
            .findFirst()
            .get();
    }
}
