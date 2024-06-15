package htmake.jtbot.discord.commands.poll.data;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Option {

    private String title;

    private int votes;

    private String turnout;

    private Set<String> votingUser;
}
