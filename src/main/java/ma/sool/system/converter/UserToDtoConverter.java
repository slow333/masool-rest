package ma.sool.system.converter;

import ma.sool.hoguser.HogUser;
import ma.sool.hoguser.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToDtoConverter implements Converter<HogUser, UserDto> {
  @Override
  public UserDto convert(HogUser source) {
    UserDto dto = new UserDto(
            source.getId(),
            source.getUsername(),
            source.isEnabled(),
            source.getRoles());
    return dto;
  }
}
