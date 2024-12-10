package ccbs.conf.base;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class RptApiInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(jakarta.servlet.ServletContext servletContext) throws ServletException {

		// Profile setting
	    servletContext.setInitParameter("spring.profiles.active", "prd");
	        
	    System.out.println("spring.profiles.active is : "+servletContext.getInitParameter("spring.profiles.active"));

        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

}
