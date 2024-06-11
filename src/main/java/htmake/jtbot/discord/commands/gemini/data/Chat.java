package htmake.jtbot.discord.commands.gemini.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Chat {

    private String question;

    private List<String> answerList;

    private int maxPage;
}
