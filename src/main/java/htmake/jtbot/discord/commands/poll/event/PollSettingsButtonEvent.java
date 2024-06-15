package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.util.PollUtil;
import htmake.jtbot.discord.util.ErrorUtil;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class PollSettingsButtonEvent {

    private final PollUtil pollUtil;
    private final ErrorUtil errorUtil;

    public PollSettingsButtonEvent() {
        this.pollUtil = new PollUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, int pollId) {
        Pair<Boolean, String> check = pollUtil.pollValidCheck(pollId, event.getUser().getId());

        if (!check.getFirst()) {
            errorUtil.sendError(event.getHook(), "투표 설정", check.getSecond());
        }

        MessageEmbed embed = buildEmbed();

        String messageId = event.getMessage().getId();

        InteractionHook hook = event.getHook().setEphemeral(true);
        hook.sendMessageEmbeds(embed)
                .setActionRow(
                        Button.secondary("poll-edit-" + pollId + "-" + messageId, "투표 수정"),
                        Button.secondary("poll-end-" + pollId + "-" + messageId, "투표 종료"),
                        Button.secondary("poll-delete-" + pollId + "-" + messageId, "투표 삭제")
                )
                .queue();
    }

    private MessageEmbed buildEmbed() {
        return new EmbedBuilder()
                .setColor(Color.DARK_GRAY)
                .setTitle(":gear: 설정")
                .setDescription("원하시는 설정 옵션을 선택해주세요.")
                .build();
    }
}
