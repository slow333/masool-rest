package ma.sool.art;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtRepo extends JpaRepository<Art, String> {
}
