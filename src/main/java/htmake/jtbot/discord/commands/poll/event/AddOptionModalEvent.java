package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.discord.commands.poll.data.Option;
import htmake.jtbot.discord.commands.poll.data.Poll;
import htmake.jtbot.discord.commands.poll.util.PollUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AddOptionModalEvent {

    private static final String DEFAULT_TURNOUT = "`                    ` | 0% (0)";

    private final PollUtil pollUtil;

    private final PollCache pollCache;

    public AddOptionModalEvent() {
        this.pollUtil = new PollUtil();

        this.pollCache = CacheFactory.pollCache;
    }

    public void execute(ModalInteractionEvent event, int pollId) {
        String optionName = event.getValue("option").getAsString();

        addNewOption(pollId, optionName);

        MessageEmbed updatedEmbed  = buildEmbed(optionName, event.getMessage().getEmbeds().get(0));
        List<ActionRow> actionRowList = buildActionRowList(event.getMessage().getButtons(), pollId, pollCache.get(pollId).getLastIndex());

        event.getHook().editOriginalEmbeds(updatedEmbed)
                .setComponents(actionRowList)
                .queue();
    }

    private void addNewOption(int pollId, String optionName) {
        Poll poll = pollCache.get(pollId);
        int newIndex = poll.getLastIndex() + 1;

        Option option = Option.builder()
                .title(optionName)
                .votes(0)
                .turnout(DEFAULT_TURNOUT)
                .votingUser(new HashSet<>())
                .build();

        poll.getOptionById().put(newIndex, option);
        poll.setLastIndex(newIndex);
    }

    private MessageEmbed buildEmbed(String optionName, MessageEmbed originalEmbed) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(originalEmbed.getTitle())
                .setDescription(originalEmbed.getDescription());

        originalEmbed.getFields().forEach(embedBuilder::addField);

        embedBuilder.addField(pollUtil.formatTitle(originalEmbed.getFields().size() + 1, optionName), DEFAULT_TURNOUT, false);

        return embedBuilder.build();
    }

    private List<ActionRow> buildActionRowList(List<Button> buttonList, int pollId, int newIndex) {
        List<ActionRow> actionRowList = new ArrayList<>();
        List<Button> newButtonList = new ArrayList<>();

        int size = buttonList.size();

        for (int i = 0; i < size - 2; i++) {
            newButtonList.add(buttonList.get(i));
        }

        String emoji = pollUtil.getUnicode(newIndex);
        Button newButton = Button.secondary("poll-voting-" + pollId + "-" + newIndex, "0").withEmoji(Emoji.fromUnicode(emoji));
        newButtonList.add(newButton);

        if (newButtonList.size() < 10) {
            newButtonList.add(buttonList.get(size - 2));
        }
        newButtonList.add(buttonList.get(size - 1));

        pollUtil.setActionRowList(actionRowList, newButtonList);

        return actionRowList;
    }
}
