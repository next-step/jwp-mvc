package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    @DisplayName("파라미터로 범용적인 객체 생성 방법")
    @Test
    void newInstanceTest() {
        Question question = newInstance(Question.class, "writer", "title", "contents");
        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("contents");

    }

    public static <T> T newInstance(Class<T> clazz, Object... args) {

        Constructor constructor = null;

        for (Constructor candidate : clazz.getConstructors()) {
            if (candidate.getParameterCount() == args.length) {
                constructor = candidate;
            }
        }

        if (constructor == null) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " doesn't have args size constructor");
        }

        try {
            return clazz.cast(constructor.newInstance(args));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("{} instantiation failed ", clazz.getName());
            throw new IllegalArgumentException(clazz.getName());
        }
    }

}
