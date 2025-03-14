package ma.sool.hoguser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Setter
@Getter
@Data
public class HogUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "username은 필수입니다.")
  private String username;

  @NotEmpty(message = "암호는 필수입니다.")
  private String password;

  private boolean enabled;

  @NotEmpty(message = "roles는 필수입니다.")
  private String roles; // 공백으로 구분(admin user)
}
