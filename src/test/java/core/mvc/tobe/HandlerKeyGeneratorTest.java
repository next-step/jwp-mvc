package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyGeneratorTest {

    @DisplayName("컨트롤러에 적용된 @RequestMapping 의 value 를 메서드 @RequestMapping value 의 prefix 로 적용한다")
    @Test
    void apply_controller_value_as_the_prefix_of_the_method_value() {
        final RequestMapping controllerAnnotation = controllerAnnotationWithValue();
        final RequestMapping methodAnnotation = methodAnnotationWithValue();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(controllerAnnotation, methodAnnotation);

        final List<HandlerKey> expected = Arrays.stream(RequestMethod.values())
            .map(it -> new HandlerKey("/controllerPath/methodPath", it))
            .collect(Collectors.toList());

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("컨트롤러에 적용된 @RequestMapping 이 없으면 메서드 @RequestMapping 의 value 만 허용한다 ")
    @Test
    void method_value_without_controller_request_mapping() {
        final RequestMapping methodAnnotation = methodAnnotationWithValue();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(null, methodAnnotation);

        final List<HandlerKey> expected = Arrays.stream(RequestMethod.values())
            .map(it -> new HandlerKey("/methodPath", it))
            .collect(Collectors.toList());

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("클래스에 적용된 RequestMethod 와 메서드에 적용된 RequestMethod 를 모두 허용한다.")
    @Test
    void both_the_class_request_method_and_the_method_request_method_are_allowed() {
        final RequestMapping controllerAnnotation = controllerAnnotationWithValueWithGetMethod();
        final RequestMapping methodAnnotation = methodAnnotationWithValueWithPostMethod();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(controllerAnnotation, methodAnnotation);

        final List<HandlerKey> expected = List.of(
            new HandlerKey("/controllerPath/methodPath", RequestMethod.GET),
            new HandlerKey("/controllerPath/methodPath", RequestMethod.POST)
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("컨트롤러에 적용된 @RequestMapping 이 없으면 메서드에 적용된 RequestMethod 만 허용한다.")
    @Test
    void the_method_request_methods_are_allowed_without_controller_request_methods() {
        final RequestMapping methodAnnotation = methodAnnotationWithValueWithPostMethodAndGetMethod();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(null, methodAnnotation);

        final List<HandlerKey> expected = List.of(
            new HandlerKey("/methodPath", RequestMethod.GET),
            new HandlerKey("/methodPath", RequestMethod.POST)
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("클래스에 적용된 RequestMethod 는 있지만 메서드에 적용된 RequestMethod 가 없다면 클래스의 RequestMethod 만 허용한다.")
    @Test
    void only_the_request_methods_of_the_class_is_allowed() {
        final RequestMapping controllerAnnotation = controllerAnnotationWithValueWithGetMethod();
        final RequestMapping methodAnnotation = methodAnnotationWithValue();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(controllerAnnotation, methodAnnotation);

        final List<HandlerKey> expected = List.of(
            new HandlerKey("/controllerPath/methodPath", RequestMethod.GET)
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("클래스에 적용된 RequestMethod 가 없다면 메서드에 적용된 RequestMethod 만 허용한다")
    @Test
    void only_the_request_methods_of_the_method_is_allowed() {
        final RequestMapping controllerAnnotation = controllerAnnotationWithValue();
        final RequestMapping methodAnnotation = methodAnnotationWithValueWithPostMethodAndGetMethod();

        final List<HandlerKey> actual = HandlerKeyGenerator.generate(controllerAnnotation, methodAnnotation);

        final List<HandlerKey> expected = List.of(
            new HandlerKey("/controllerPath/methodPath", RequestMethod.GET),
            new HandlerKey("/controllerPath/methodPath", RequestMethod.POST)
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    private static RequestMapping controllerAnnotationWithValue() {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String value() {
                return "/controllerPath";
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[]{};
            }
        };
    }

    private static RequestMapping controllerAnnotationWithValueWithGetMethod() {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String value() {
                return "/controllerPath";
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[]{RequestMethod.GET};
            }
        };
    }

    private static RequestMapping methodAnnotationWithValue() {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String value() {
                return "/methodPath";
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[]{};
            }
        };
    }

    private static RequestMapping methodAnnotationWithValueWithPostMethod() {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String value() {
                return "/methodPath";
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[]{RequestMethod.POST};
            }
        };
    }

    private static RequestMapping methodAnnotationWithValueWithPostMethodAndGetMethod() {
        return new RequestMapping() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String value() {
                return "/methodPath";
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[]{RequestMethod.POST, RequestMethod.GET};
            }
        };
    }
}
