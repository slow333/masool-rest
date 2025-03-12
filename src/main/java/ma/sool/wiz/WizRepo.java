package ma.sool.wiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WizRepo extends JpaRepository<Wiz, Integer> {
}
