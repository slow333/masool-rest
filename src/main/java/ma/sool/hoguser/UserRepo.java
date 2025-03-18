package ma.sool.hoguser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<HogUser, Long> {
    Optional<HogUser> findByUsername(String username);
}
