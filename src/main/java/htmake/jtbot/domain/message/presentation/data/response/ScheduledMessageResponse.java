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
public class ScheduledMessageResponse {

    private String guildId;

    private String channelId;

    private String userName;

    private String userAvatar;

    private String message;

    public static ScheduledMessageResponse toResponse (ScheduledMessage scheduledMessage) {
        return ScheduledMessageResponse.builder()
            .guildId(scheduledMessage.getGuildId())
            .channelId(scheduledMessage.getChannelId())
            .userName(scheduledMessage.getUserName())
            .userAvatar(scheduledMessage.getUserAvatar())
            .message(scheduledMessage.getMessage())
            .build();
    }
}
