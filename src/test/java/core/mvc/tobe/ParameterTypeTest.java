package core.mvc.tobe;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParameterTypeTest {

    @Test
    void casting() {
        assertThat(ParameterTypeEnum.casting("1", int.class)).isEqualTo(1);
        assertThat(ParameterTypeEnum.casting("1", long.class)).isEqualTo(1L);
        assertThat(ParameterTypeEnum.casting("1", Integer.class)).isEqualTo(1);
        assertThat(ParameterTypeEnum.casting("1", Long.class)).isEqualTo(1L);
        assertThat(ParameterTypeEnum.casting("1", String.class)).isEqualTo("1");
    }
}