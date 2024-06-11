package htmake.jtbot.discord.commands.gemini;

import htmake.jtbot.discord.commands.gemini.event.GeminiChatButtonEvent;
import htmake.jtbot.discord.commands.gemini.event.GeminiChatSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminiCommand extends ListenerAdapter {

    private final GeminiChatSlashEvent geminiChatSlashEvent;

    private final GeminiChatButtonEvent geminiChatButtonEvent;

    public GeminiCommand() {
        this.geminiChatSlashEvent = new GeminiChatSlashEvent();

        this.geminiChatButtonEvent = new GeminiChatButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("호떡아")) {
            geminiChatSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("gemini")) {
            if (componentList.get(1).equals("chat")){
                int chatId = Integer.parseInt(componentList.get(2));
                int page = Integer.parseInt(componentList.get(3));
                geminiChatButtonEvent.execute(event, chatId, page);
            }
        }
    }
}
