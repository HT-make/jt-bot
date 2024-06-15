package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.util.PollUtil;
import htmake.jtbot.discord.util.ErrorUtil;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class AddOptionButtonEvent {

    private final ErrorUtil errorUtil;
    private final PollUtil pollUtil;

    public AddOptionButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.pollUtil = new PollUtil();
    }

    public void execute(ButtonInteractionEvent event, int pollId) {
        Pair<Boolean, String> check = pollUtil.pollValidCheck(pollId, event.getUser().getId());

        if (!check.getFirst()) {
            event.deferEdit().queue();
            errorUtil.sendError(event.getHook(), "투표 항목 추가", check.getSecond());
            return;
        }

        Modal modal = buildModal(pollId);
        event.replyModal(modal).queue();
    }

    private Modal buildModal(int pollId) {
        TextInput textInput = TextInput.create("option", "투표 항목", TextInputStyle.SHORT)
                .setPlaceholder("투표 항목을 입력해주세요.")
                .setRequired(true)
                .build();

        return Modal.create("poll-add-" + pollId, "투표 항목 추가")
                .addActionRow(textInput)
                .build();
    }
}
