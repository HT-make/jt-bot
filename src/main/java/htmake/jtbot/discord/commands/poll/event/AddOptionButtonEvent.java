package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class AddOptionButtonEvent {

    private final ErrorUtil errorUtil;

    private final PollCache pollCache;

    public AddOptionButtonEvent() {
        this.errorUtil = new ErrorUtil();

        this.pollCache = CacheFactory.pollCache;
    }

    public void execute(ButtonInteractionEvent event, int pollId) {
        if (!pollCache.containsKey(pollId)) {
            event.deferEdit().queue();
            errorUtil.sendError(event.getHook(), "투표 항목 추가", "투표를 찾을 수 없습니다.");
            return;
        }

        String author = pollCache.get(pollId).getAuthor();
        String userId = event.getUser().getId();

        if (!author.equals(userId)) {
            event.deferEdit().queue();
            errorUtil.sendError(event.getHook(), "투표 항목 추가", "권한이 없습니다.");
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
