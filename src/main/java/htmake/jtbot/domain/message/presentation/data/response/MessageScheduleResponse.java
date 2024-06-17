package htmake.jtbot.domain.message.presentation.data.response;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageScheduleResponse {

    private long id;

    private String message;

    private String scheduledTime;

    public static MessageScheduleResponse toResponse (ScheduledMessage scheduledMessage) {
        return MessageScheduleResponse.builder()
            .id(scheduledMessage.getId())
            .message(scheduledMessage.getMessage())
            .scheduledTime(scheduledMessage.getScheduledTime())
            .build();
    }
}
