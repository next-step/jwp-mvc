package core.mvc.tobe.handler.resolver.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimpleTypeConverterTest {

    @Test
    void parse() {
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Long convert = simpleTypeConverter.convert("3", Long.class);

        assertThat(convert).isEqualTo(3L);
    }
}