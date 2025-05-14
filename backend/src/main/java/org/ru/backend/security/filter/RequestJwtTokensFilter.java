package org.ru.backend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.dto.LoginRequestDTO;
import org.ru.backend.dto.TokenResponseDTO;
import org.ru.backend.security.Token;
import org.ru.backend.security.access.DefaultAccessTokenFactory;
import org.ru.backend.security.refresh.DefaultRefreshTokenFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Function;

@Setter
@Slf4j
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/login", HttpMethod.POST.name());

    private Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory();

    private Function<Token, String> refreshTokenStringSerializer = Object::toString;

    private Function<Token, String> accessTokenStringSerializer = Object::toString;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            try {
                LoginRequestDTO loginRequest = objectMapper.readValue(
                        request.getInputStream(),
                        LoginRequestDTO.class
                );


                if (StringUtils.isBlank(loginRequest.getUsername())
                        || StringUtils.isBlank(loginRequest.getPassword())) {
                    throw new BadCredentialsException("Empty credentials");
                }


                UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

                if (!userDetails.isEnabled()) {
                    throw new BadCredentialsException("User is not enabled");
                }

                if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                        throw new BadCredentialsException("Invalid password");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());


                var refreshToken = this.refreshTokenFactory.apply(authentication);
                var accessToken = this.accessTokenFactory.apply(refreshToken);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(
                        response.getWriter(),
                        new TokenResponseDTO(
                                accessTokenStringSerializer.apply(accessToken),
                                accessToken.expiresAt().toString(),
                                refreshTokenStringSerializer.apply(refreshToken),
                                refreshToken.expiresAt().toString()
                        )
                );
                return;

            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request format");
            } catch (UsernameNotFoundException | BadCredentialsException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            }
            return;
        }
        filterChain.doFilter(request, response);
    }

}

