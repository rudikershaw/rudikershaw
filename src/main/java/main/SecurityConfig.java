package main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Security configuration for the application. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Whether to use SSL as pull from the application properties. */
    private final boolean useSsl;

    public SecurityConfig(@Value("${use.ssl}") final boolean useSsl) {
        this.useSsl = useSsl;
    }

    /** The number of seconds in a year. */
    private static final int ONE_YEAR_IN_SECONDS = 31556926;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        if (useSsl) {
            http.redirectToHttps(Customizer.withDefaults())
                .headers(
                    headers -> headers.httpStrictTransportSecurity(
                        hsts -> hsts.maxAgeInSeconds(ONE_YEAR_IN_SECONDS)
                    )
                );
        }
        return http.build();
    }
}
