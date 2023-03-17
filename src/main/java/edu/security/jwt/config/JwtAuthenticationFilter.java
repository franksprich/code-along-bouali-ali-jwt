package edu.security.jwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {DESCRIPTION}
 *
 * @author Frank Sprich
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VAlUE_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTH_HEADER_KEY);
        final String jwt;
        final String userEmail;
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VAlUE_PREFIX)) {
            /* Starting from AUTH_PREFIX.length() (7) because of jwt is appended to AUTH_PREFIX ("Bearer ") */
            jwt = authHeader.substring(AUTH_HEADER_VAlUE_PREFIX.length());
            // Check if the authentication has taken place already to not repeat it every time request is sent
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userEmail = jwtService.extractUsername(jwt);
                if (userEmail != null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                    // Check if the token is still valid
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Update the security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        // Always call the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
