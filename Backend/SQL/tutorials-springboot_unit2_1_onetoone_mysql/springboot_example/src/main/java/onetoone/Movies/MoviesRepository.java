package onetoone.Movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */

public interface MoviesRepository extends JpaRepository<Movies, Long> {

    /**
     * gets movie with given id
     * @param id movie id
     * @return movie with given id
     */
    Movies findById(int id);

    /**
     * deletes movie with given id
     * @param id movie id
     */
    @Transactional
    void deleteById(int id);
}
