package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.discord.commands.poll.data.Option;
import htmake.jtbot.discord.commands.poll.data.Poll;
import htmake.jtbot.discord.commands.poll.util.PollUtil;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PollSlashEvent {

    private final ErrorUtil errorUtil;
    private final PollUtil pollUtil;

    private final PollCache pollCache;

    public PollSlashEvent() {
        this.errorUtil = new ErrorUtil();
        this.pollUtil = new PollUtil();

        this.pollCache = CacheFactory.pollCache;
    }

    public void execute(SlashCommandInteractionEvent event) {
        String title = event.getOption("주제").getAsString();
        List<String> optionList = List.of(event.getOption("항목").getAsString().split(","));
        String description = event.getOption("설명") != null ? event.getOption("설명").getAsString() : "";
        boolean duplication = event.getOption("중복-응답-허용") != null && (event.getOption("중복-응답-허용").getAsString().equals("true"));

        if (optionList.size() > 10) {
            errorUtil.sendError(event, "투표 생성", "투표 항목은 최대 10개까지 생성할 수 있습니다.");
            return;
        }

        savePollCache(event.getUser().getId(), title, optionList, description, duplication);

        MessageEmbed embed = buildEmbed(title, optionList, description);
        List<ActionRow> actionRowList = buildActionRowList(optionList.size());

        event.replyEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private void savePollCache(String id, String title, List<String> optionList, String description, boolean duplication) {
        Poll poll = Poll.builder()
                .author(id)
                .title(title)
                .description(description)
                .duplication(duplication)
                .totalVotes(0)
                .options(toOptions(optionList))
                .totalVotingUsers(new HashSet<>())
                .lastIndex(optionList.size())
                .build();

        int index = pollCache.getIndex();
        pollCache.put(index, poll);
        pollCache.setIndex(index + 1);
    }

    private Map<Integer, Option> toOptions(List<String> optionList) {
        Map<Integer, Option> options = new HashMap<>();

        int index = 1;

        for (String title : optionList) {
            Option option = Option.builder()
                    .title(title)
                    .votes(0)
                    .votingUser(new HashSet<>())
                    .build();

            options.put(index++, option);
        }

        return options;
    }

    private MessageEmbed buildEmbed(String title, List<String> optionList, String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":bar_chart:" + title)
                .setDescription(description);

        for (int i = 1; i <= optionList.size(); i++) {
            String option = optionList.get(i - 1);
            embedBuilder.addField(pollUtil.formatTitle(i, option), "`                    ` | 0% (0)", false);
        }

        return embedBuilder.build();
    }

    private List<ActionRow> buildActionRowList(int size) {
        List<ActionRow> actionRowList = new ArrayList<>();
        List<Button> buttonList = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            String emoji = pollUtil.getUnicode(i);
            buttonList.add(Button.secondary("poll-" + pollCache.getIndex() + "-" + i, "0").withEmoji(Emoji.fromUnicode(emoji)));

            if (i % 5 == 0) {
                actionRowList.add(ActionRow.of(buttonList));
                buttonList = new ArrayList<>();
            }
        }

        if (!buttonList.isEmpty()) {
            actionRowList.add(ActionRow.of(buttonList));
        }

        return actionRowList;
    }
}
