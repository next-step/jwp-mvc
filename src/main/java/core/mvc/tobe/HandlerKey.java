package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class HandlerKey {

    private final String requestMappingUri;
    private final RequestMethod requestMethod;
}
