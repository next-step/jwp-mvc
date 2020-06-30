package core.utils.typeresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
class PrimitiveTypeResolversTest {

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Boolean 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void booleanTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, boolean.class)).isEqualTo(Boolean.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Boolean 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b"})
    void booleanTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, boolean.class)
        ).withMessage(BoolTypeResolver.ILLEGAL_BOOLEAN + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Byte 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"byte1", "byte2"})
    void byteTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, byte.class)).isEqualTo(inputBoolean.getBytes());
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Character 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b"})
    void charTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, char.class)).isEqualTo(inputBoolean.charAt(0));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Character 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"ab", "bads"})
    void charTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, char.class)
        ).withMessage(CharacterTypeResolver.ILLEGAL_CHAR + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Double 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0.01", "0.0000001", "1"})
    void doubleTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, double.class)).isEqualTo(Double.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Double 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b"})
    void doubleTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, double.class)
        ).withMessage(DoubleTypeResolver.ILLEGAL_DOUBLE + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Float 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1f", "0.0000001f"})
    void floatTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, float.class)).isEqualTo(Float.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Float 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b"})
    void floatTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, float.class)
        ).withMessage(FloatTypeResolver.ILLEGAL_FLOAT + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Integer 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void integerTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, int.class)).isEqualTo(Integer.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Integer 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0.0001", "1f", "a", "b"})
    void integerTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, int.class)
        ).withMessage(IntegerTypeResolver.ILLEGAL_INTEGER + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Long 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void longTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, long.class)).isEqualTo(Long.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Long 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0.0001", "1f", "a", "b"})
    void longTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, long.class)
        ).withMessage(LongTypeResolver.ILLEGAL_LONG + inputBoolean);
    }

    @DisplayName("String으로 들어온 값을 그 값에 맞는 Short 타입으로 변환 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void shortTypeResolverTest(String inputBoolean) {
        assertThat(PrimitiveTypeResolvers.resolve(inputBoolean, short.class)).isEqualTo(Short.valueOf(inputBoolean));
    }

    @DisplayName("String으로 들어온 값이 유효하지 않은 Short 값일때 예외를 던지는 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0.0001", "1f", "a", "b"})
    void shortTypeResolverExceptionTest(String inputBoolean) {
        assertThatIllegalArgumentException().isThrownBy(() ->
            PrimitiveTypeResolvers.resolve(inputBoolean, short.class)
        ).withMessage(ShortTypeResolver.ILLEGAL_SHORT + inputBoolean);
    }
}