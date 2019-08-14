package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicHandlerMapping {

    private List<HandlerMapping> handlerMappings;


    public BasicHandlerMapping(){
        handlerMappings = new ArrayList<>();
    }

    public void addMapping(HandlerMapping handlerMapping){
        handlerMappings.add(handlerMapping);
        handlerMapping.initMapping();
    }

    public ModelAndView find(HttpServletRequest req, HttpServletResponse resp){
        return handlerMappings.stream()
                .map(handlerMapping -> {
                    try{
                        return handlerMapping.findAndExecute(req, resp);
                    }catch (Exception e){
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
