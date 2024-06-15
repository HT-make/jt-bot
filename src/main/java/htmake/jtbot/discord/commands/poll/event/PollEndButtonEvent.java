package htmake.jtbot.discord.commands.poll.event;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.discord.commands.poll.data.Option;
import htmake.jtbot.discord.commands.poll.data.Poll;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PollEndButtonEvent {

    private final PollCache pollCache;

    public PollEndButtonEvent() {
        this.pollCache = CacheFactory.pollCache;
    }

    public void execute(ButtonInteractionEvent event, int pollId, String messageId) {
        Poll poll = pollCache.get(pollId);

        List<Option> optionList = new ArrayList<>(poll.getOptionById().values());
        optionList.sort(Comparator.comparingInt(Option::getVotes).reversed());

        String channelId = event.getChannelId();
        TextChannel channel = event.getJDA().getTextChannelById(channelId);

        channel.retrieveMessageById(messageId).queue(message -> {
            MessageEmbed embed = buildEmbed(message.getEmbeds().get(0), optionList);
            message.editMessageEmbeds(embed).queue();
            message.editMessageComponents().queue();
        });

        String guildId = event.getGuild().getId();
        MessageEmbed embed = buildAlertEmbed(poll.getTitle(), guildId, channelId, messageId);
        channel.sendMessageEmbeds(embed).queue();

        event.getHook().deleteOriginal().queue();
    }

    private MessageEmbed buildEmbed(MessageEmbed embed, List<Option> optionList) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(embed.getTitle() + " 결과")
                .setDescription(embed.getDescription());

        for (int i = 0; i < optionList.size(); i++) {
            Option option = optionList.get(i);

            String name = (i < 3 ? getEmoji(i) : "");
            name += option.getTitle();

            embedBuilder.addField(name, option.getTurnout(), false);
        }

        return embedBuilder.build();
    }

    private String getEmoji(int number) {
        return switch (number) {
            case 0 -> ":first_place: ";
            case 1 -> ":second_place: ";
            case 2 -> ":third_place: ";
            default -> null;
        };
    }

    private MessageEmbed buildAlertEmbed(String title, String guildId, String channelId, String messageId) {
        String hyperLink = "[" + title + "](https://discord.com/channels/" + guildId + "/" + channelId + "/" + messageId + ")";

        return new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(":bell: 투표 종료")
                .setDescription(hyperLink + "에 대한 투표가 종료되었습니다.\n어서 결과를 확인하러 가보세요!")
                .build();
    }
}
