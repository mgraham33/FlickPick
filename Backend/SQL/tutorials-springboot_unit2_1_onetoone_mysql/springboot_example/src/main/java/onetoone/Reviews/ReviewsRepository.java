package onetoone.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

    /**
     * gets review with given id
     * @param id review id
     * @return review with given id
     */
    Reviews findById(int id);

    /**
     * deletes review with given id
     * @param id review id
     */
    @Transactional
    void deleteById(int id);
}
