package next.support.context;

import core.jdbc.ConnectionManager;
import core.mvc.Environment;
import core.mvc.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        initContext(sce);
        logger.info("Completed Load ServletContext!");
    }

    private void initContext(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.init(sce.getServletContext());

        servletContext.setAttribute(ApplicationContext.class.getName(), applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext context = (ApplicationContext) sce.getServletContext().getAttribute(ApplicationContext.class.getName());

        if (context != null) {
            context.destroy();
        }
    }
}
