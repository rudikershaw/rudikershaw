package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** The Code Nerd application configuration. */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class CodenerdApplication implements WebMvcConfigurer {

    /** The time to cache images in milli-seconds. */
    private static final int CACHE_TIME = 604800;

    /**
     * The application starts here.
     * @param args parameters for starting up the application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(CodenerdApplication.class, args);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(CACHE_TIME);
    }
}
