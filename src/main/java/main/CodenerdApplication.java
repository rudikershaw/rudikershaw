package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

/** The Code Nerd application configuration. */
@SpringBootApplication
public class CodenerdApplication extends WebMvcConfigurerAdapter {

    private static final int CACHE_TIME = 604800;

    /** Start here. */
    public static void main(String[] args) {
        SpringApplication.run(CodenerdApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(CACHE_TIME);
    }
}
