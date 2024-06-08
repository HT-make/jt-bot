package htmake.jtbot.discord.commands.global;

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

import java.util.List;

@Component
public class GlobalCommand extends ListenerAdapter {

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
                Commands.slash("호떡아", "호떡이에게 질문합니다.").addOptions(insertQuestion()),
                Commands.slash("투표", "투표를 생성합니다.").addOptions(createPollForm())
        );

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

    private OptionData insertQuestion() {
        return new OptionData(OptionType.STRING, "질문", "질문을 입력해 주세요!")
                .setRequired(true)
                .setRequiredLength(1, 1000);
    }

    private List<OptionData> createPollForm() {
        OptionData title = new OptionData(OptionType.STRING, "주제", "투표 주제를 입력해 주세요!")
                .setRequired(true)
                .setRequiredLength(1, 50);

        OptionData optionList = new OptionData(OptionType.STRING, "항목", "투표 항목을 입력해주세요!(최대: 10) 예시: 사과, 바나나, 수박")
                .setRequired(true);

        OptionData description = new OptionData(OptionType.STRING, "설명", "투표에 대한 설명을 입력해주세요!")
                .setRequired(false);

        OptionData duplication = new OptionData(OptionType.STRING, "중복-응답-허용", "중복 응답 허용 여부")
                .setRequired(false)
                .addChoice("예", "true")
                .addChoice("아니오", "false");

        return List.of(title, optionList, description, duplication);
    }
}