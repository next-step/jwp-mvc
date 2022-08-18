package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.mvc.tobe.support.utils.PathPatternUtils;

public class PathPatternUtilsTest {

	@Test
	@DisplayName("Path 추출 테스트")
	public void getValue() {
		String actual1 = PathPatternUtils.getValue("/users/{id}", "/users/1", "id");
		String actual2 = PathPatternUtils.getValue("/users/{name}", "/users/javajigi", "name");

		assertThat(actual1).isEqualTo("1");
		assertThat(actual2).isEqualTo("javajigi");
	}
}
