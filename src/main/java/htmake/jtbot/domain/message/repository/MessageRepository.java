package htmake.jtbot.domain.message.repository;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<ScheduledMessage, Long> {
     List<ScheduledMessage> findByUserId(String userId);

    @Query("SELECT COUNT(sm) FROM ScheduledMessage sm WHERE sm.userId = :userId")
    int countByUserId(@Param("userId") String userId);

    List<ScheduledMessage> findByScheduledTime(String scheduledTime);
}
