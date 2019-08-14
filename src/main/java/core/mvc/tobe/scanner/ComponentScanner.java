package core.mvc.tobe.scanner;

import com.google.common.collect.Sets;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author : yusik
 * @date : 2019-08-10
 */
public class ComponentScanner {

    public static Set<Class<?>> scan(String[] basePackages, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> scannedAnnotations = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            scannedAnnotations.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return scannedAnnotations;
    }
}
