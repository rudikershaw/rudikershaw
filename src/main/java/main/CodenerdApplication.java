package main;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.moesif.servlet.MoesifFilter;

/** The Code Nerd application configuration. */
@Configuration
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

    /**
     * Configure the Moesif analytics filter.
     * @return the Filter.
     */
    @Bean
    public Filter moesifFilter() {
        return new MoesifFilter("eyJhcHAiOiI2MTc6MjUxIiwidmVyIjoiMi4wIiwib3JnIjoiMjA3OjI2NCIsI"
                                + "mlhdCI6MTU3MTI3MDQwMH0.k7mv34JLYi4NPBPzP4RMJnzSyFmCc2z1uJrxm-VrMgg");
    }
}
