package core.mvc.tobe.utils;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public interface ParameterSupport {

    ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    static ParameterNameDiscoverer nameDiscoverer() {
        return NAME_DISCOVERER;
    }

}
