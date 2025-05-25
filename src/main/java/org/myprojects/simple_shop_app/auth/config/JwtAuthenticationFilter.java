package org.myprojects.simple_shop_app.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.myprojects.simple_shop_app.auth.model.user.UserDetailsAdapter;
import org.myprojects.simple_shop_app.auth.service.JwtService;
import org.myprojects.simple_shop_app.auth.service.TokenManagerService;
import org.myprojects.simple_shop_app.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenManagerService tokenManagerService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        String jwtToken = authHeader.substring(7);

        String userEmail = jwtService.extractUsernameFromToken(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!tokenManagerService.doesAccessTokenExist(jwtToken) || !jwtService.isTokenValid(jwtToken)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Невалидный или просроченный токен");

                return;
            }

            var user = userService.getUserByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

            var userDetails = UserDetailsAdapter.builder().user(user).build();

            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
