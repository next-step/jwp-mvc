package core.di.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClassNewInstanceUtils {

    public static Optional<ConstructorParameters> getContructorParameters(Class<?> clazz, String... parameterNames) {
        return getContructorParameters(clazz, Arrays.asList(parameterNames));
    }
    
    public static Optional<ConstructorParameters> getContructorParameters(Class<?> clazz, List<String> parameterNames) {
        return Arrays.asList(clazz.getConstructors())
                .stream()
                .map(ParameterNameDiscoverUtils::toConstructorParameters)
                .filter(cp -> cp.isMatchedParamNames(parameterNames))
                .findFirst();
    }

}
