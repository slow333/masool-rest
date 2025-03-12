package ma.sool.system.converter;

import ma.sool.art.Art;
import ma.sool.art.ArtDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtToDtoConverter implements Converter<Art, ArtDto> {

  private final WizToDtoConverter wizToDtoConverter;

  ArtToDtoConverter(WizToDtoConverter wizToDtoConverter) {
    this.wizToDtoConverter = wizToDtoConverter;
  }

  @Override
  public ArtDto convert(Art source) {
    return new ArtDto(
            source.getId(),
            source.getName(),
            source.getDescription(),
            source.getImgUrl(),
            source.getOwner() != null
                    ? this.wizToDtoConverter.convert(source.getOwner())
                    : null
            );
  }
}
