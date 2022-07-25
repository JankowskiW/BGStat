package pl.wj.bgstat.security.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCredentialsDto {
    private String username;
    private String password;
}
