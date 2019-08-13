package core.mvc.tobe;

import core.mvc.tobe.mock.MockRequestParam;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodParameterTest {

    @Test
    void methodParameter() {
        MethodParameter stringMp = new MethodParameter(String.class, new Annotation[]{new MockRequestParam()});
        MethodParameter integerMp = new MethodParameter(Integer.class, new Annotation[]{new MockRequestParam()});
        MethodParameter intMp = new MethodParameter(int.class, new Annotation[]{new MockRequestParam()});

        assertTrue(stringMp.isString());
        assertTrue(integerMp.isInteger());
        assertTrue(intMp.isInteger());
    }

}
