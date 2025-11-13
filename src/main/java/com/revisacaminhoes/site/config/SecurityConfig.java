package com.revisacaminhoes.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ========================================================================
     * CADEIA DE FILTROS 1: ROTAS PÚBLICAS (Prioridade 1)
     * ========================================================================
     */
    @Bean
    @Order(1) // <<< RODA PRIMEIRO
    public SecurityFilterChain publicApiSecurity(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Define quais rotas esta cadeia deve capturar
                .securityMatcher(
                        "/api/auth/password/**",
                        "/api/email/compramos-seu-batido",
                        "/api/email/fale-conosco",
                        "/api/produtos/**",
                        "/api/produtos",
                        "/api/produtos/ativos",
                        "/api/produtos/filtro",
                        "/api/produto-fotos/**",
                        "/api/marcas/**",
                        "/api/modelos/**",
                        "/api/modelosativos",
                        "/api/site/settings/**",
                        "/error"
                        // <<< REMOVIDO: "/api/**" (era o que estava quebrando o login)
                )

                .authorizeHttpRequests(auth -> auth
                        // O CorsFilter (em CorsConfig.java) já cuida do OPTIONS
                        .anyRequest().permitAll()
                )

                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.sendError(401)));

        return http.build();
    }

    /**
     * ========================================================================
     * CADEIA DE FILTROS 2: ROTAS PRIVADAS (Prioridade 2)
     * ========================================================================
     */
    @Bean
    @Order(2) // <<< RODA DEPOIS
    public SecurityFilterChain privateApiSecurity(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Captura todas as rotas da API (que não foram capturadas pela Cadeia 1)
                .securityMatcher("/api/**")

                .authorizeHttpRequests(auth -> auth
                        // (As rotas públicas já foram capturadas pela Cadeia 1)
                        // A rota /api/auth/check vai cair aqui agora e será autenticada

                        // Regras de Admin
                        .requestMatchers("/api/auth/register-email").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(withDefaults())

                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.sendError(401)));

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}