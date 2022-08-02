package core.di.factory;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;

public class ComponentScanTest {
	private static final Logger logger = LoggerFactory.getLogger(ComponentScanTest.class);

	private Reflections reflections;

	@BeforeEach
	public void setUp() {
		reflections = new Reflections("core.di.factory.example");
	}

	@Test
	@DisplayName("@Controller Annotation 클래스 찾기")
	public void findControllerAnnotation() {
		Set<Class<?>> typesAnnotation = reflections.getTypesAnnotatedWith(Controller.class);

		typesAnnotation.stream().forEach(annotation -> logger.debug("Class Name : {}", annotation.getName()));
	}

	@Test
	@DisplayName("@Service Annotation 클래스 찾기")
	public void findServiceAnnotation() {
		Set<Class<?>> typesAnnotation = reflections.getTypesAnnotatedWith(Service.class);

		typesAnnotation.stream().forEach(annotation -> logger.debug("Class Name : {}", annotation.getName()));
	}

	@Test
	@DisplayName("@Repository Annotation 클래스 찾기")
	public void findRepositoryAnnotation() {
		Set<Class<?>> typesAnnotation = reflections.getTypesAnnotatedWith(Repository.class);

		typesAnnotation.stream().forEach(annotation -> logger.debug("Class Name : {}", annotation.getName()));
	}
}
