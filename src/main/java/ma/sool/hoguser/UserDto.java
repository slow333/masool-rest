package ma.sool.hoguser;


import jakarta.validation.constraints.NotEmpty;

public record UserDto(Long id,

                      @NotEmpty(message = "username 필수")
                      String username,

                      boolean enabled,

                      @NotEmpty(message = "Roles required")
                      String roles

                      )
{}
