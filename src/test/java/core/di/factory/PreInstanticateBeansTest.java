package core.di.factory;

import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PreInstanticateBeansTest {

    @Test
    public void create() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(QnaController.class);
        classes.add(JdbcUserRepository.class);

        PreInstanticateBeans preInstanticateBeans = new PreInstanticateBeans(classes);

        Map<Class<?>, Object> map = preInstanticateBeans.createBeanObject();
        assertThat(map).isNotEqualTo(null);
    }
}
