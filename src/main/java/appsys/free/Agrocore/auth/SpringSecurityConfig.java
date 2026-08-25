package appsys.free.Agrocore.auth;

import appsys.free.Agrocore.auth.JwtAuthenticationFilter;
import appsys.free.Agrocore.auth.JwtValidationFilter;
import appsys.free.Agrocore.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private JwtValidationFilter jwtValidationFilter;

    @Autowired
    private IUserService userService; // ← AGREGAR

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authManager) throws Exception {

        // ← CAMBIO: pasar userService al constructor
        JwtAuthenticationFilter jwtAuthFilter =
                new JwtAuthenticationFilter(authManager, userService);
        jwtAuthFilter.setFilterProcessesUrl("/api/login");

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/dispositivos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/dispositivos/getDispos").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/periodo/{sinc}").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/info/validConexion").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/info/repConexion").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/info/statusApp").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/info/extLicenc").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/movil/validUser").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(jwtAuthFilter)
                .addFilterBefore(jwtValidationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Arrays.asList("*"));
        configuration.setAllowedMethods(
                java.util.Arrays.asList("GET","POST","PUT","DELETE", "PATCH","OPTIONS"));
        configuration.setAllowedHeaders(
                java.util.Arrays.asList("Content-Type","Authorization"));
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}