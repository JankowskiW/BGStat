package pl.wj.bgstat.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.login.model.LoginCredentialsDto;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentialsDto credentials) {
    }

}
