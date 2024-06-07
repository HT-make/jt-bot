package htmake.jtbot.discord.commands.global;

import htmake.jtbot.discord.util.ErrorUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Component
public class GlobalCommand extends ListenerAdapter {

    private final ErrorUtil errorUtil;

    public GlobalCommand() {
        this.errorUtil = new ErrorUtil();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        event.deferEdit().queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event){
        List<CommandData> commandData = List.of(
                Commands.slash("호떡아", "호떡이에게 질문합니다.").addOptions(insertQuestion().setRequired(true).setRequiredLength(1, 1000))
        );
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

    private OptionData insertQuestion() {
        return new OptionData(OptionType.STRING, "질문", "질문을 입력해 주세요!");
    }
}