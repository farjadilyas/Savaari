package com.savaari_demo.api.filters;

import com.savaari_demo.api.JWTUtil;
import com.savaari_demo.api.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/rider/*")
@Configuration
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Check if header contains Bearer <jwt>
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        /* Ignore these */
        String path = ((HttpServletRequest) request).getRequestURI();
        if (!(path.startsWith("/rider") || path.startsWith("/driver") || path.startsWith("/admin"))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            System.out.println("We're in this OPT1");

            if (jwtUtil.validateToken(jwt, userDetails)) {
                System.out.println("We're in this OPT2");

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // Hand off control to next filter
                filterChain.doFilter(request, response);
            }
        }
        else {
            System.out.println("We're in this else block");
        }
    }
}
