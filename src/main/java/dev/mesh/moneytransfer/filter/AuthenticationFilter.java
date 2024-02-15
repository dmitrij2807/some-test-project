package dev.mesh.moneytransfer.filter;

import dev.mesh.moneytransfer.service.SecurityService;
import dev.mesh.moneytransfer.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  public static final String BEARER_PREFIX = "Bearer ";
  public static final String HEADER_NAME = "Authorization";
  private final SecurityService securityService;
  private final UserService userService;

  public AuthenticationFilter(SecurityService securityService, UserService userService) {
    this.securityService = securityService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    var authHeader = request.getHeader(HEADER_NAME);
    if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    var jwt = authHeader.substring(BEARER_PREFIX.length());
    var username = securityService.extractUserName(jwt);
    var userId = securityService.extractUserId(jwt);

    if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService
          .userDetailsService()
          .loadUserByUsername(username);

      if (securityService.isTokenValid(jwt, userDetails)) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authToken.setDetails(Map.of("userId", userId));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(request, response);
  }
}
