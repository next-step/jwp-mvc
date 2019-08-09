package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Annotation[] annotations = methods[i].getAnnotations();
            if(isAnnotationClass(MyTest.class, annotations)){
                methods[i].invoke(clazz.newInstance());
            }
        }
    }

    public boolean isAnnotationClass(Class<?> annotationClass, Annotation[] annotations){
        for (int i = 0; i < annotations.length; i++) {
            return annotations[i].annotationType().equals(annotationClass);
        }
        return false;
    }
}
