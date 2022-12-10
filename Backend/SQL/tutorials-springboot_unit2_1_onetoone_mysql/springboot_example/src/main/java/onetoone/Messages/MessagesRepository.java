package onetoone.Messages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Chris Costa
 */
public interface MessagesRepository extends JpaRepository<Messages, Long> {

    Messages findById(int id);

    @Transactional
    void deleteById(int id);
}
