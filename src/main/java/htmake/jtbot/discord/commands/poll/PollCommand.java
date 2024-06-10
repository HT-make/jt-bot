package htmake.jtbot.discord.commands.poll;

import htmake.jtbot.discord.commands.poll.event.PollButtonEvent;
import htmake.jtbot.discord.commands.poll.event.PollSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PollCommand extends ListenerAdapter {

    private final PollSlashEvent pollSlashEvent;

    private final PollButtonEvent pollButtonEvent;

    public PollCommand() {
        this.pollSlashEvent = new PollSlashEvent();

        this.pollButtonEvent = new PollButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("투표")) {
            pollSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("poll")) {
            int pollId = Integer.parseInt(componentList.get(1));
            int optionId = Integer.parseInt(componentList.get(2));
            pollButtonEvent.execute(event, pollId, optionId);
        }
    }
}
