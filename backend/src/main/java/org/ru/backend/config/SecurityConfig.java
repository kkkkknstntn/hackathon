package org.ru.backend.config;

import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.RequiredArgsConstructor;
import org.ru.backend.enums.Authorities;
import org.ru.backend.repository.DeactivatedTokenRepository;
import org.ru.backend.repository.UserRepository;
import org.ru.backend.security.TokenAuthenticationUserDetailsService;
import org.ru.backend.security.filter.JwtLogoutFilter;
import org.ru.backend.security.filter.RefreshTokenFilter;
import org.ru.backend.security.filter.RequestJwtTokensFilter;
import org.ru.backend.security.filter.JwtAuthenticationFilter;
import org.ru.backend.security.jwt.access.AccessTokenJwsStringDeserializer;
import org.ru.backend.security.jwt.access.AccessTokenJwsStringSerializer;
import org.ru.backend.security.jwt.refresh.RefreshTokenJweStringDeserializer;
import org.ru.backend.security.jwt.refresh.RefreshTokenJweStringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

    @Value("${jwt.access-token-key}")
    private String accessTokenKey;

    @Value("${jwt.refresh-token-key}")
    private String refreshTokenKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .disabled(!user.getIsEnabled())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream()
                                .map(Authorities::name)
                                .map(SimpleGrantedAuthority::new)
                                .toList())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Bean
    public RequestJwtTokensFilter requestJwtTokensFilter(UserDetailsService userDetailsService) throws Exception {
        var filter = new RequestJwtTokensFilter();
        filter.setUserDetailsService(userDetailsService);
        filter.setPasswordEncoder(passwordEncoder());
        filter.setAccessTokenStringSerializer(new AccessTokenJwsStringSerializer(
                new MACSigner(OctetSequenceKey.parse(accessTokenKey))
        ));
        filter.setRefreshTokenStringSerializer(new RefreshTokenJweStringSerializer(
                new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
        ));
        return filter;
    }

    @Bean
    public RefreshTokenFilter refreshTokenFilter() throws Exception {
        var filter = new RefreshTokenFilter();
        filter.setAccessTokenStringSerializer(new AccessTokenJwsStringSerializer(
                new MACSigner(OctetSequenceKey.parse(accessTokenKey))
        ));
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   RequestJwtTokensFilter requestJwtTokensFilter,
                                                   DeactivatedTokenRepository deactivatedTokenRepository,
                                                   RefreshTokenFilter refreshTokenFilter) throws Exception {

        var jwtAuthenticationFilter = new JwtAuthenticationFilter(
                new AccessTokenJwsStringDeserializer(new MACVerifier(OctetSequenceKey.parse(accessTokenKey))),
                new RefreshTokenJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))),
                authenticationManager
        );

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);

        var jwtLogoutFilter = new JwtLogoutFilter(deactivatedTokenRepository);

        return http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/activation/activate").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/users", HttpMethod.POST.name())).permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .build();
    }
}
