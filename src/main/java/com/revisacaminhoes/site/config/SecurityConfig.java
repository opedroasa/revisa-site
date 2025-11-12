package com.revisacaminhoes.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService; // seu UsuarioService
        this.passwordEncoder = passwordEncoder;       // vem do PasswordConfig
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults()) // <<< habilita CORS usando o bean abaixo
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/register-email").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/password/**").permitAll()
                        .requestMatchers("/api/auth/check").permitAll()


                        // libera preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")

                        // Páginas públicas
                        .requestMatchers(HttpMethod.GET,
                                "/api/produtos/**",
                                "/api/produtos",          // listagem
                                "/api/produtos/ativos",   // sua lista de ativos
                                "/api/produtos/filtro",   // se existir
                                "/api/produto-fotos/**",  // se expõe imagem pública
                                "/api/marcas/**",
                                "/api/modelos/**",
                                "/api/modelosativos",
                                "/api/site/settings/**",
                                "/error"
                        ).permitAll()

                        // Páginas públicas (POST) - Formulários de Contato/Email
                        // Essas regras DEVEM vir ANTES das regras restritivas de POST
                        .requestMatchers(HttpMethod.POST,
                                "/api/email/compramos-seu-batido",
                                "/api/email/fale-conosco"
                        ).permitAll()
                        // Fim da correção

                        // Mutações só admin
                        .requestMatchers(HttpMethod.POST,   "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // 401 sem popup de login do browser
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.sendError(401)))
                .httpBasic(withDefaults());

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
