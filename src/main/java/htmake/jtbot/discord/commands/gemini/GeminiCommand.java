package htmake.jtbot.discord.commands.gemini;

import htmake.jtbot.discord.commands.gemini.event.GeminiChatSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class GeminiCommand extends ListenerAdapter {

    private final GeminiChatSlashCommand geminiChatSlashCommand;

    public GeminiCommand() {
        this.geminiChatSlashCommand = new GeminiChatSlashCommand();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("호떡아")) {
            geminiChatSlashCommand.execute(event);
        }
    }
}
