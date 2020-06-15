package core.mvc.tobe;

import com.google.common.collect.Maps;
import next.util.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtilsTest.class);

    @Test
    @DisplayName("MultiValued Map에서 메소드 파라미터 추출 테스트")
    void extractFromMultiValuedMap() throws Exception {
        String userId = "ninjasul";
        String password = "1234";
        int age = 42;

        Map<String, String[]> parameters = getMultiValuedParameters(userId, password, age);

        Field[] fields = TestUser.class.getDeclaredFields();

        for (Field field : fields) {
            log.debug("fieldName: {}, fieldType: {}", field.getName(), field.getType());
        }

        Object[] extracted = ReflectionUtils.extractFromMultiValuedMap(parameters, fields);

        log.debug("extracted: {}", StringUtils.toPrettyJson(extracted));

        TestUser user = (TestUser) ReflectionUtils.newInstance(TestUser.class, extracted);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getAge()).isEqualTo(age);
    }

    @Test
    @DisplayName("빈 MultiValued Map에서 메소드 파라미터 추출 테스트")
    void extractFromEmptyMultiValuedMap() throws Exception {
        Map<String, String[]> parameters = getMultiValuedParameters(null, null, -1);

        Field[] fields = TestUser.class.getDeclaredFields();

        for (Field field : fields) {
            log.debug("fieldName: {}, fieldType: {}", field.getName(), field.getType());
        }

        Object[] extracted = ReflectionUtils.extractFromMultiValuedMap(parameters, fields);

        log.debug("extracted: {}", StringUtils.toPrettyJson(extracted));

        TestUser user = (TestUser) ReflectionUtils.newInstance(TestUser.class, extracted);

        assertThat(user).isNull();
    }

    private Map<String, String[]> getMultiValuedParameters(
        String userId,
        String password,
        int age
    ) {
        // Parameter Map은 순서가 보장되지 않으므로 LinkedHashMap으로 테스트
        Map<String, String[]> parameters = Maps.newLinkedHashMap();
        parameters.put("password", new String[] {password});

        if (age > 0) {
            parameters.put("age", new String[]{String.valueOf(age)});
        }
        parameters.put("userId", new String[] {userId});
        return parameters;
    }
}