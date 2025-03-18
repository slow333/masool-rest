package ma.sool.security;

import lombok.RequiredArgsConstructor;
import ma.sool.hoguser.HogUser;
import ma.sool.hoguser.MyUserPrincipal;
import ma.sool.hoguser.UserDto;
import ma.sool.system.converter.UserToDtoConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserToDtoConverter userToDtoConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info.
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogUser hogUser = principal.getHogUser();
        UserDto userDto = userToDtoConverter.convert(hogUser);
        // Create a JWT.
        String token = jwtProvider.createToken(authentication);
        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("userInfo", userDto);
        loginResult.put("token", token);

        return loginResult;
    }
}
