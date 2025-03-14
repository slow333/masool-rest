package ma.sool.system.converter;

import ma.sool.hoguser.HogUser;
import ma.sool.hoguser.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToEntityConverter implements Converter<UserDto, HogUser> {

  @Override
  public HogUser convert(UserDto source) {
    HogUser user = new HogUser();
    user.setUsername(source.username());
    user.setEnabled(source.enabled());
    user.setRoles(source.roles());
    return user;
  }
}
