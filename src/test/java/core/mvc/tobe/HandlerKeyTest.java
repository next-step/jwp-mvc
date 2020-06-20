package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HandlerKeyTest {


    @Test
    void test_equality() {
        final RequestMethod rm = RequestMethod.GET;
        final String uri1 = "/test/{id}";
        final String uri2 = "/test/1234";

        final HandlerKey hk1 = new HandlerKey(uri1, rm);
        final HandlerKey hk2 = new HandlerKey(uri2, rm);

        assertThat(hk1).isEqualTo(hk2);
    }
}