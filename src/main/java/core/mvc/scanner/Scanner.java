package core.mvc.scanner;

import java.util.Set;

public interface Scanner {

    void scan(Object[] basePackage);

    Set<Class<?>> getScannedClasses();

    Object getInstance(Class<?> clazz);


}
