package htmake.jtbot.discord.commands.poll;

import htmake.jtbot.discord.commands.poll.event.PollSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PollCommand extends ListenerAdapter {

    private final PollSlashEvent pollSlashEvent;

    public PollCommand() {
        this.pollSlashEvent = new PollSlashEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("투표")) {
            pollSlashEvent.execute(event);
        }
    }
}
