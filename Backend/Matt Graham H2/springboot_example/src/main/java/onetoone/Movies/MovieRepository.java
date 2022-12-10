package onetoone.Movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(int id);

    @Transactional
    void deleteById(int id);
}
