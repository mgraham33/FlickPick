package onetoone.Links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Chris Costa and Matt Graham
 */

public interface LinksRepository extends JpaRepository<Links, Long> {

    /**
     * gets link set with given id
     * @param id link set id
     * @return link set with given id
     */
    Links findById(int id);

    /**
     * deletes a link set with given id
     * @param id link set id
     */
    @Transactional
    void deleteById(int id);
}

