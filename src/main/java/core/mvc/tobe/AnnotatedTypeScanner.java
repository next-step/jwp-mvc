package core.mvc.tobe;

import com.google.common.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iltaek on 2020/06/17 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class AnnotatedTypeScanner {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedTypeScanner.class);

    private static final String FAIL_NEW_INSTANCE = "다음의 클래스의 객체 생성에 실패하였습니다. : %s";
    private static final Map<Class<?>, Object> instanceCache = Maps.newHashMap();

    private AnnotatedTypeScanner() {
    }

    public static Set<Class<?>> getAnnotatedTypedClazz(Class<? extends Annotation> annotation, Object[] basePackage) {
        return new Reflections(basePackage,
            new SubTypesScanner(false),
            new TypeAnnotationsScanner()).getTypesAnnotatedWith(annotation);
    }

    public static Set<Method> getAnnotatedTypedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return ReflectionUtils.getMethods(clazz, ReflectionUtils.withAnnotation(annotationClass));
    }

    public static Object getClazzObject(Class<?> clazz) {
        if (instanceCache.containsKey(clazz)) {
            return instanceCache.get(clazz);
        }
        return newInstance(clazz);
    }

    private static Object newInstance(Class<?> clazz) {
        try {
            instanceCache.put(clazz, clazz.newInstance());
            return instanceCache.get(clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(String.format(FAIL_NEW_INSTANCE, clazz.getName()));
        }
    }
}
