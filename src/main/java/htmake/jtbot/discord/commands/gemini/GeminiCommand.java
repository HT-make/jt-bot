package htmake.jtbot.discord.commands.gemini;

import htmake.jtbot.discord.commands.gemini.event.GeminiChatSlashCommand;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class GeminiCommand extends ListenerAdapter {

    private final GeminiChatSlashCommand geminiChatSlashCommand;
    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public GeminiCommand() {
        this.geminiChatSlashCommand = new GeminiChatSlashCommand();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("호떡아")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            geminiChatSlashCommand.execute(event);
        }
    }
}
