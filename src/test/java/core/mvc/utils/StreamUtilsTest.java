package core.mvc.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StreamUtilsTest {

    @DisplayName("zip 테스트!")
    @Test
    void test_zipping_two_streams() {
        final Map<String, Integer> answers = new HashMap<>();
        answers.put("hyeyoom", 99);
        answers.put("javajigi", 100);
        answers.put("boorwonie", 100);
        final List<String> students = Arrays.asList("hyeyoom", "javajigi", "boorwonie");
        final List<Integer> scores = Arrays.asList(99, 100, 100);
        final List<Pair<String, Integer>> zip = StreamUtils.zip(students, scores);
        zip.forEach(pair -> {
            assertThat(pair.getSecond()).isEqualTo(answers.get(pair.getFirst()));
        });
    }
}