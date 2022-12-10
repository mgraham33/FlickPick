package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */

public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * gets user with given id
     * @param id user id
     * @return user with given id
     */
    Users findById(int id);

    /**
     * deletes user with given id
     * @param id user id
     */
    @Transactional
    void deleteById(int id);
}
