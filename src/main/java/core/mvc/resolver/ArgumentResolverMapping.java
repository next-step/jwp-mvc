package core.mvc.resolver;

import core.annotation.Component;
import core.di.factory.BeanFactory;
import core.mvc.MethodParameter;
import core.mvc.exception.NoSuchArgumentResolverException;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class ArgumentResolverMapping {
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final List<MethodArgumentResolver> resolvers = new ArrayList<>();

    public ArgumentResolverMapping() {}

    public void init() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("core.mvc.resolver");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class);

        BeanFactory beanFactory = new BeanFactory(types);
        beanFactory.initialize();

        List<MethodArgumentResolver> founded = beanFactory.getBeansFilter(MethodArgumentResolver.class::isInstance);
        this.resolvers.addAll(founded);
    }

    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        return IntStream.range(0, method.getParameterCount())
                .mapToObj(idx->{
                    MethodParameter methodParameter = MethodParameter.of(method, idx);
                    MethodArgumentResolver foundResolver = findResolver(methodParameter);

                    return foundResolver.resolve(request, response, Objects.requireNonNull(parameterNames)[idx], methodParameter);
                }).toArray();
    }

    private MethodArgumentResolver findResolver(MethodParameter methodParameter) {
        return resolvers.stream()
                .filter(resolver-> resolver.support(methodParameter))
                .findFirst()
                .orElseThrow(()-> new NoSuchArgumentResolverException(methodParameter));
    }
}
