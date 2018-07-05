package main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${use.ssl}")
    private boolean useSsl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (useSsl) {
            http.requiresChannel().anyRequest().requiresSecure();
            http.headers().httpStrictTransportSecurity().maxAgeInSeconds(31556926);
        }
    }
}
