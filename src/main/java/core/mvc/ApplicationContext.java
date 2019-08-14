package core.mvc;

import core.mvc.tobe.HandlerAdapterFactory;
import core.mvc.tobe.view.ViewResolverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.ServletContext;

public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private HandlerAdapterFactory handlerAdapterFactory;
    private ViewResolverManager viewResolverManager;
    private Environment environment;

    public void init(ServletContext servletContext) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        this.environment = new Environment();
        this.handlerAdapterFactory = new HandlerAdapterFactory(environment);
        this.viewResolverManager = new ViewResolverManager();

        servletContext.setAttribute(ApplicationContext.class.getName(), this);
        stopWatch.stop();
        logger.info("applicationContext initializing time: [" + stopWatch.getLastTaskTimeMillis() + "] millis");
    }

    public void destroy() {
        handlerAdapterFactory.destroy();
        viewResolverManager.destory();
    }

    public HandlerAdapterFactory getHandlerAdapterFactory() {
        return handlerAdapterFactory;
    }

    public ViewResolverManager getViewResolverManager() {
        return viewResolverManager;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
