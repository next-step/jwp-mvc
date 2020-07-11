package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanFactoryTest {
    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> clazzWithController = reflections.getTypesAnnotatedWith(Controller.class);

        System.out.println("@Controller 가 붙은 클래스");
        clazzWithController.forEach(clazz -> System.out.println(clazz.getSimpleName()));

        System.out.println("@Service 가 붙은 클래스");
        Set<Class<?>> clazzWithService = reflections.getTypesAnnotatedWith(Service.class);
        clazzWithService.forEach(clazz -> System.out.println(clazz.getSimpleName()));

        System.out.println("@Repository 가 붙은 클래스");
        Set<Class<?>> clazzWithRepository = reflections.getTypesAnnotatedWith(Repository.class);
        clazzWithRepository.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
