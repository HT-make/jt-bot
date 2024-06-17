package htmake.jtbot.discord.commands.message;

import htmake.jtbot.discord.commands.message.event.MessageCancelButtonEvent;
import htmake.jtbot.discord.commands.message.event.MessageCancelPageButtonEvent;
import htmake.jtbot.discord.commands.message.event.MessageInfoSlashEvent;
import htmake.jtbot.discord.commands.message.event.MessageScheduleSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageCommand extends ListenerAdapter {

    private final MessageScheduleSlashEvent messageScheduleSlashEvent;
    private final MessageInfoSlashEvent messageInfoSlashEvent;

    private final MessageCancelButtonEvent messageCancelButtonEvent;
    private final MessageCancelPageButtonEvent messageCancelPageButtonEvent;

    public MessageCommand() {
        this.messageScheduleSlashEvent = new MessageScheduleSlashEvent();
        this.messageInfoSlashEvent = new MessageInfoSlashEvent();

        this.messageCancelButtonEvent = new MessageCancelButtonEvent();
        this.messageCancelPageButtonEvent = new MessageCancelPageButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("메시지-예약")) {
            messageScheduleSlashEvent.execute(event);
        }

        if (command.equals("메시지-목록")) {
            messageInfoSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("message")) {
            event.deferEdit().queue();

            if (componentList.get(1).equals("button")){

                if (componentList.get(2).equals("cancel")) {
                    messageCancelPageButtonEvent.execute(event);
                }
            }

            if (componentList.get(1).equals("cancel")) {
                messageCancelButtonEvent.execute(event, componentList.get(2));
            }
        }
    }
}
