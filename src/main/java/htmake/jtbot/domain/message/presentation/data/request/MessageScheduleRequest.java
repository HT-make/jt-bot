package htmake.jtbot.domain.message.presentation.data.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageScheduleRequest {

    private String userId;

    private String userName;

    private String userAvatar;

    private String guildId;

    private String channelId;

    private String message;

    private String scheduledTime;
}
