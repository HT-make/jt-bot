package htmake.jtbot.discord.commands.poll.data;

import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Poll {

    private String author;

    private String title;

    private String description;

    private boolean duplication;

    private int totalVotes;

    private Map<Integer, Option> options;

    private Set<String> totalVotingUsers;

    private int lastIndex;
}
