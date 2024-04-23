/**
 * <p>
 *     Authorization Header Format:
 *      In the HTTP header, the Authorization field typically contains the JWT token.
 *      The standard format for including a JWT token in the header is: Bearer <token>.
 *      The word “Bearer” followed by a space indicates that the token follows.
 * </p>
 */
package com.userservice.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String getAuthRequestHeader = request.getHeader("Authorization");
        /* Validate the Authentication Header
        *  We stop the execution of the Security Filter Chain if
        *  a Bearer Token is not present
        * */
        if(getAuthRequestHeader == null || getAuthRequestHeader.isEmpty()
                && !getAuthRequestHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            /* Stop the execution of security filter chain here */ return;

        }

        /** Start to generate the token here using the AuthenticationResponse */
        final String username;
        final String jwt;
        // Assign the value, token, after the Bearer prefix
        // @example "Bearer <token>"
        jwt = getAuthRequestHeader.substring(7);            // Remove the Bearer prefix
        // Assign the username as sub in the Claims
        username = jwtService.extractUsername(jwt);
    }
}
