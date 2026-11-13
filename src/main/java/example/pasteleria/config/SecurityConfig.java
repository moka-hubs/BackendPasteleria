package example.pasteleria.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/registro", "/error").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers("/doc/**", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**").permitAll()

                        // Catalogo publico: la tienda se ve sin iniciar sesion
                        .requestMatchers(HttpMethod.GET, "/productos", "/productos/**", "/categorias", "/categorias/**").permitAll()

                        // Escritura del catalogo: solo administradores
                        .requestMatchers("/productos", "/productos/**", "/categorias", "/categorias/**").hasAuthority("ADMIN")

                        // CRUD de usuarios: solo administradores
                        .requestMatchers("/api/v1/usuarios", "/api/v1/usuarios/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .addFilterBefore((Filter)jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}