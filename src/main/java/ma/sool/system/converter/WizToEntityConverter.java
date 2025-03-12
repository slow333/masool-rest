package ma.sool.system.converter;

import ma.sool.wiz.Wiz;
import ma.sool.wiz.WizDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizToEntityConverter implements Converter<WizDto, Wiz> {

  @Override
  public Wiz convert(WizDto source) {
    Wiz wiz = new Wiz();
    wiz.setId(source.id());
    wiz.setName(source.name());
    return wiz;
  }
}
