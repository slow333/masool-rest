package ma.sool.art;

import jakarta.validation.constraints.NotEmpty;
import ma.sool.wiz.WizDto;

public record ArtDto(String id,

                     @NotEmpty(message="name 필수항목")
                     String name,

                     @NotEmpty(message="description 필수항목")
                     String description,

                     @NotEmpty(message="image url 필수항목")
                     String imgUrl,

                     WizDto owner)
{}
