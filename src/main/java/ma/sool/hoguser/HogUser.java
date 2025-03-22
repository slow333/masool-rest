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

  @NotEmpty(message = "username is required.")
  private String username;

  @NotEmpty(message = "password is required.")
  private String password;

  private boolean enabled;

  @NotEmpty(message = "roles is required.")
  private String roles; // 공백으로 구분(admin user)
}
