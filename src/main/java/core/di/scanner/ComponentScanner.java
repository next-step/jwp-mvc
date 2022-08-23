package core.di.scanner;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ComponentScanner {

    private static final ComponentScanner componentScanner = new ComponentScanner();

    private ComponentScanner() {

    }

    public static ComponentScanner getInstance() {
        return componentScanner;
    }

    public Set<Class<?>> getComponents() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("core"))
                .setScanners(Scanners.TypesAnnotated));

        // TODO ApplicationContext Custom
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        // Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        // Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        return controllers;
    }

}
