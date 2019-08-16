package core.mvc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ViewResolversTest {

    @Test
    void jspView() {
        ViewResolvers viewResolvers = new ViewResolvers();
        assertThat(viewResolvers.getView("form.jsp")).isExactlyInstanceOf(JspView.class);
    }

    @Test
    void redirectView() {
        ViewResolvers viewResolvers = new ViewResolvers();
        assertThat(viewResolvers.getView("redirect:form.jsp")).isExactlyInstanceOf(RedirectView.class);
    }
}