package ma.sool.wiz;


import jakarta.validation.constraints.NotEmpty;
import ma.sool.art.Art;

import java.util.List;

public record WizDto(
        Integer id, 
        @NotEmpty(message = "이름은 필수입니다.")
        String name, 
        Integer numberOfArts) {
}
