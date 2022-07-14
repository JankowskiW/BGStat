package pl.wj.bgstat.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final long accessTokenExpirationTime = 300000;
    private static final long refreshTokenExpirationTime = 600000;
    private static final String secret = "Rz&cbX#$23&wGaEy1QQT82ngf#8FQort5ZPY$1e3ADf6b4EMgH";

//    public RestAuthenticationSuccessHandler(@Value("${jwt.accessTokenExpirationTime}") long accessTokenExpirationTime,
//                                            @Value("${jwt.refreshTokenExpirationTime}") long refreshTokenExpirationTime,
//                                            @Value("${jwt.secret}") String secret) {
//        this.accessTokenExpirationTime = accessTokenExpirationTime;
//        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
//        this.secret = secret;
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String accessToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .sign(algorithm);

        response.addHeader("access_token", "Bearer " + accessToken);
        response.addHeader("refresh_token",  "Bearer " +  refreshToken);
    }
}
