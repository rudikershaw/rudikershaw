package main.configuration;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** A filter that returns a 410 Gone status for specified paths. */
@Component
public class GoneFilter extends OncePerRequestFilter {

    /** The set of paths that should return a 410 Gone status. */
    private final Set<String> gonePaths;

    public GoneFilter(final @Value("${app.gone.paths}")Set<String> gonePaths) {
        this.gonePaths = gonePaths;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (gonePaths.contains(request.getRequestURI())) {
            response.sendError(HttpServletResponse.SC_GONE); // 410
        }
        filterChain.doFilter(request, response);
    }
}