package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.discord.commands.poll.data.Option;
import htmake.jtbot.discord.commands.poll.data.Poll;
import htmake.jtbot.discord.commands.poll.util.PollUtil;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PollButtonEvent {

    private static final int MAX_BAR_LENGTH = 20;
    private static final int BAR_SEGMENT_SIZE = 5;
    private static final String FILLED_CELL = "▮";
    private static final String EMPTY_CELL = "▯";

    private final ErrorUtil errorUtil;
    private final PollUtil pollUtil;

    private final PollCache pollCache;

    public PollButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.pollUtil = new PollUtil();

        this.pollCache = CacheFactory.pollCache;
    }

    public void execute(ButtonInteractionEvent event, int pollId, int optionId) {
        String userId = event.getUser().getId();

        if (!pollCache.containsKey(pollId)) {
            errorUtil.sendError(event.getHook(), "투표", "해당 투표를 이용할 수 없습니다.");
            return;
        }

        Poll poll = pollCache.get(pollId);
        Option option = poll.getOptionById().get(optionId);
        Set<String> votingUser = option.getVotingUser();

        if (votingUser.contains(userId)) {
            removeVote(poll, option, userId);
        } else {
            handleVote(poll, option, userId);
        }
        MessageEmbed embed = buildEmbed(poll, event.getMessage().getEmbeds().get(0));
        List<ActionRow> actionRowList = buildActionRowList(event.getMessage().getButtons(), poll.getOptionById().values());

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private void removeVote(Poll poll, Option option, String userId) {
        poll.setTotalVotes(poll.getTotalVotes() - 1);
        option.setVotes(option.getVotes() - 1);
        option.getVotingUser().remove(userId);
    }

    private void handleVote(Poll poll, Option option, String userId) {
        if (!poll.isDuplication()) {
            handleNonDuplicateVote(poll, option, userId);
        }

        poll.setTotalVotes(poll.getTotalVotes() + 1);
        option.setVotes(option.getVotes() + 1);
        option.getVotingUser().add(userId);
    }

    private void handleNonDuplicateVote(Poll poll, Option option, String userId) {
        Map<String, Option> optionByUser = poll.getOptionByUser();

        if (optionByUser.containsKey(userId)) {
            Option previousOption = optionByUser.get(userId);
            removeVote(poll, previousOption, userId);
        }

        optionByUser.put(userId, option);
    }

    private MessageEmbed buildEmbed(Poll poll, MessageEmbed embed) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(embed.getTitle())
                .setDescription(embed.getDescription());

        int totalVotes = poll.getTotalVotes();
        Collection<Option> options = poll.getOptionById().values();

        int index = 1;

        for (Option option : options) {
            String formattedTitle = pollUtil.formatTitle(index++, option.getTitle());
            String bar = drawBar(totalVotes, option.getVotes());
            embedBuilder.addField(formattedTitle, bar, false);
        }

        return embedBuilder.build();
    }

    private String drawBar(int totalVotes, int votes) {
        if (totalVotes == 0) {
            return getBar(0) + " | 0% (" + votes + ")";
        }

        double percentages = ((double) votes / totalVotes) * 100;
        int adjustPercentages = adjustPercentages((int) percentages);
        int drawCell = adjustPercentages / BAR_SEGMENT_SIZE;

        String formattedPercentages = adjustPercentages == percentages ? String.valueOf(adjustPercentages) : String.format("%.2f", percentages);

        return getBar(drawCell) + " | " + formattedPercentages + "% (" + votes + ")";
    }

    private String getBar(int filledCells) {
        StringBuilder bar = new StringBuilder(MAX_BAR_LENGTH);

        for (int i = 0; i < MAX_BAR_LENGTH; i++) {
            bar.append(i < filledCells ? FILLED_CELL : EMPTY_CELL);
        }

        return bar.toString();
    }

    private int adjustPercentages(int percentages) {
        int units = percentages % 10;
        int adjustedUnits;

        if (units >= 1 && units < 3) {
            adjustedUnits = 0;
        } else if (units >= 3 && units < 7) {
            adjustedUnits = 5;
        } else if (units >= 7 && units < 9){
            adjustedUnits = 10;
        } else {
            adjustedUnits = 0;
        }

        return (percentages / 10) * 10 + adjustedUnits;
    }

    private List<ActionRow> buildActionRowList(List<Button> buttonList, Collection<Option> options) {
        List<ActionRow> actionRowList = new ArrayList<>();
        List<Button> newButtonList = new ArrayList<>();

        int index = 0;

        for (Option option : options) {
            Button button = buttonList.get(index);
            newButtonList.add(Button.secondary(button.getId(), String.valueOf(option.getVotes())).withEmoji(button.getEmoji()));

            if (++index % 5 == 0) {
                actionRowList.add(ActionRow.of(newButtonList));
                newButtonList = new ArrayList<>();
            }
        }

        if (!newButtonList.isEmpty()) {
            actionRowList.add(ActionRow.of(newButtonList));
        }

        return actionRowList;
    }
}
