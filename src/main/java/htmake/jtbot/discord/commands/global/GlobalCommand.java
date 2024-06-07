package htmake.jtbot.discord.commands.global;

import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.discord.util.MessageUtil;
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
    private final MessageUtil messageUtil;

    public GlobalCommand() {
        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();

        String component = event.getComponentId();

        if (component.equals("cancel")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            messageUtil.remove(event.getUser().getId());

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setDescription("작업이 취소되었습니다.")
                    .build();

            event.getHook().editOriginalComponents(Collections.emptyList()).queue();
            event.getHook().editOriginalEmbeds(embed).queue();
        }
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