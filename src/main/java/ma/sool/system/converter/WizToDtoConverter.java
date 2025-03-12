package ma.sool.system.converter;

import ma.sool.wiz.Wiz;
import ma.sool.wiz.WizDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizToDtoConverter implements Converter<Wiz, WizDto> {

  @Override
  public WizDto convert(Wiz source) {
    return new WizDto(source.getId(),
            source.getName(),
            source.getNumberOfArts());
  }
}
