package pl.wj.bgstat.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.domain.user.UserService;
import pl.wj.bgstat.domain.user.model.User;
import pl.wj.bgstat.role.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pl.wj.bgstat.security.JwtAuthorizationFilter.TOKEN_PREFIX;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final long ACCESS_TOKEN_EXP_TIME;
    private final String SECRET;

    public SecurityController(UserDetailsService userDetailsService, UserService userService,
                              @Value("${jwt.access-token-expiration-time}") long accessTokenExpTime,
                              @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.ACCESS_TOKEN_EXP_TIME = accessTokenExpTime;
        this.SECRET = secret;
    }

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(AUTHORIZATION);
        if (refreshToken != null && refreshToken.startsWith(TOKEN_PREFIX)) {
            String username = JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(refreshToken.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (username != null) {
                User user = userService.getUser(username);

                Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                response.setHeader("access_token", "Bearer " + accessToken);
                response.setHeader("refresh_token", "Bearer " + refreshToken);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }


    }
}
