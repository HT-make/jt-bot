package htmake.jtbot.discord.commands.message.event;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageResponse;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SendScheduledMessageEvent {

    private final JDA jda;

    public SendScheduledMessageEvent() {
        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        this.jda = JDABuilder.createDefault(token).build();
    }

    public void execute(List<ScheduledMessageResponse> scheduledMessageList) {
        List<ScheduledMessage> messages = toMessageList(scheduledMessageList);

        for (ScheduledMessage message : messages) {
            MessageChannel channel;

            if (message.getGuildId() == null) {
                channel = jda.getPrivateChannelById(message.getChannelId());
            } else {
                channel = jda.getTextChannelById(message.getChannelId());
            }

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(message.getUserName(), null, message.getUserAvatar())
                    .addField("", message.getMessage(), false);

            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    private List<ScheduledMessage> toMessageList(List<ScheduledMessageResponse> messageList) {
        List<ScheduledMessage> messages = new ArrayList<>();

        for (ScheduledMessageResponse messageResponse : messageList) {
            ScheduledMessage message = ScheduledMessage.builder()
                    .guildId(messageResponse.getGuildId())
                    .channelId(messageResponse.getChannelId())
                    .userName(messageResponse.getUserName())
                    .userAvatar(messageResponse.getUserAvatar())
                    .message(messageResponse.getMessage())
                    .build();
            messages.add(message);
        }

        return messages;
    }
}
