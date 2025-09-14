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

@Configuration
@EnableMethodSecurity // caso queira usar @PreAuthorize no futuro
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService; // é o seu UsuarioService
        this.passwordEncoder = passwordEncoder;       // vem do PasswordConfig
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ROTAS PÚBLICAS (consulta)
        // GET de produtos, fotos, marcas e modelos liberados
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        //.requestMatchers("/api/auth/register").permitAll() // para bootstrap inicial
                        .requestMatchers("/api/auth/register").hasRole("ADMIN") // depois de criado usuarios

                        // Produtos - consulta pública
                        .requestMatchers(HttpMethod.GET, "/api/produtos/**").permitAll()
                        // Marcas e Modelos - consulta pública
                        .requestMatchers(HttpMethod.GET, "/api/marcas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/modelos/**").permitAll()
                        // Fotos de produto - listagem pública
                        .requestMatchers(HttpMethod.GET, "/api/produtos/*/fotos/**").permitAll()

                        // TUDO QUE É DE ALTERAÇÃO REQUER ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        // Qualquer outra rota precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // para testar fácil no Postman (Basic Auth)

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // seu UsuarioService
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
