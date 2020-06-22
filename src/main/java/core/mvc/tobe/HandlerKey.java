package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;
}
