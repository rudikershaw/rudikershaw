package main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Security configuration for the application. */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    /** Whether to use SSL as pull from the application properties. */
    @Value("${use.ssl}")
    private boolean useSsl;

    /** The number of seconds in a year. */
    private static final int ONE_YEAR_IN_SECONDS = 31556926;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        if (useSsl) {
            http.requiresChannel().anyRequest().requiresSecure();
            http.headers().httpStrictTransportSecurity().maxAgeInSeconds(ONE_YEAR_IN_SECONDS);
        }
        return http.build();
    }
}
