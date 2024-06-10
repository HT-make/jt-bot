package htmake.jtbot.discord.commands.poll.data;

import lombok.*;

import java.util.Map;

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

    private Map<Integer, Option> optionById;

    private Map<String, Option> optionByUser;

    private int lastIndex;
}
