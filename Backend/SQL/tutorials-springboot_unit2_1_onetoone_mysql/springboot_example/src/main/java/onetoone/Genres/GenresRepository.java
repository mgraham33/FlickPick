package onetoone.Genres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */

public interface GenresRepository extends JpaRepository<Genres, Long> {

    /**
     * finds a genre set with given id
     * @param id id of genre set
     * @return genre set with given id
     */
    Genres findById(int id);

    /**
     * deletes genre set with given id
     * @param id id of genre set
     */
    @Transactional
    void deleteById(int id);
}
