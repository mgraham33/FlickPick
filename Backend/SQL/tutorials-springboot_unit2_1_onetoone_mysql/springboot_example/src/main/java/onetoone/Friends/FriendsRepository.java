package onetoone.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */


public interface FriendsRepository extends JpaRepository<Friends, Long> {

    /**
     * gets a friendship by id
     * @param id id of friendship
     * @return friendship with given id
     */
    Friends findById(int id);

    /**
     * deletes a friendship with a given id
     * @param id id of friendship
     */
    @Transactional
    void deleteById(int id);
}
