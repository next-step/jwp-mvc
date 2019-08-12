package core.mvc.tobe;

import core.mvc.asis.Controller;
import core.mvc.asis.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class BasicHandlerMapping implements HandlerMapping{

    private List<HandlerMapping> mappingList;

    public BasicHandlerMapping(String basePackage){
        mappingList = new ArrayList<>();
        HandlerMapping controllerMapping = new RequestMapping();
        HandlerMapping annotationMapping = new AnnotationHandlerMapping(basePackage);

        mappingList.add(controllerMapping);
        mappingList.add(annotationMapping);
    }

    @Override
    public void initMapping() {
        mappingList.stream().forEach(handlerMapping -> {
            handlerMapping.initMapping();
        });
    }

    @Override
    public Controller findController(HttpServletRequest req){
        for(HandlerMapping mapping : mappingList){
            Controller returnObject = mapping.findController(req);
            if(returnObject != null){
                return returnObject;
            }

        }

        return null;
    }
}
