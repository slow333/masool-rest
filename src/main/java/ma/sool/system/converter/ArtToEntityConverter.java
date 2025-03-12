package ma.sool.system.converter;

import ma.sool.art.Art;
import ma.sool.art.ArtDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtToEntityConverter implements Converter<ArtDto, Art> {

  @Override
  public Art convert(ArtDto source) {
    Art art = new Art();
    art.setId(source.id());
    art.setName(source.name());
    art.setDescription(source.description());
    art.setImgUrl(source.imgUrl());
    return art;
  }
}
