package htmake.jtbot.discord.commands.message.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Message {

    private long messageId;

    private Integer buttonId;
}
