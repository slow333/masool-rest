package ma.sool.security;

import lombok.RequiredArgsConstructor;
import ma.sool.hoguser.UserService;
import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/users")
public class AuthController {

    private final AuthService authService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

   @PostMapping("/login")
   public Result getLoginInfo(Authentication authentication) {
        LOGGER.info("Auth name: '{}'", authentication.getName());

        return new Result(true, StatusCode.SUCCESS,
                "UserInfo and JSON web token",
                authService.createLoginInfo(authentication));
    }
}
