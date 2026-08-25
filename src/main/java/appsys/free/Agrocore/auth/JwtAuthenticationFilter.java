package appsys.free.Agrocore.auth;

import appsys.free.Agrocore.auth.entity.Rol;
import appsys.free.Agrocore.auth.entity.UserApp;
import appsys.free.Agrocore.auth.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private IUserService userService;

    public static final String SECRET_KEY = "constrAppSecretKey2024xyzABCDEFGH";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Map<?, ?> credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), Map.class);

            String username = credentials.get("username").toString();
            String password = credentials.get("password").toString();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        User userSecurity = (User) authResult.getPrincipal();
        UserApp userApp = userService.findByUserName(userSecurity.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id",          userApp.getId());
        claims.put("noDocumento", userApp.getNoDocument());
        claims.put("nombres",     userApp.getNames());
        claims.put("apellidos",   userApp.getSurnames());
        claims.put("roles",       getRoles(userApp.getRoles()));

        var key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        String token = Jwts.builder()
                .subject(userSecurity.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 24h
                .signWith(key)
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");

        Map<String, String> body = new HashMap<>();
        body.put("access_token", token);
        body.put("token_type",   "Bearer");
        body.put("username",     userSecurity.getUsername());

        new ObjectMapper().writeValue(response.getWriter(), body);
    }

    private String getRoles(List<Rol> roles) {
        StringBuilder rolesString = new StringBuilder();
        for (Rol rol : roles) {
            rolesString.append(rol.getName()).append(",");
        }
        return rolesString.toString();
    }
}