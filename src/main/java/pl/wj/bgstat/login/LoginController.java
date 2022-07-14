package pl.wj.bgstat.login;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.bgstat.login.model.LoginCredentialsDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @PostMapping("")
    public void login(@RequestBody LoginCredentialsDto credentials) {
        // Only for swagger
    }
}
