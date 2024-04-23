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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

        /** Check if the User is authenticated and connected with authorities */
        if(username != null &&
                // If the user is not authenticated then, proceed to use the username and password
                // credentials to log our user in the application
                SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load-up UserDetails using the assigned username extracted from the Claim
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the generated JWT token
            if( jwtService.isTokenGenValid(jwt, userDetails) ) {
                System.out.println("jwt valid");
                // Fill in the Security Context
                // These are the required values since
                // we are implementing the UserDetails service
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );
                /** Set Authentication Context once the user is authenticated with a VALID JWT token */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        /** Continue filtering requests and responses */
        filterChain.doFilter(request, response);
    }
}
