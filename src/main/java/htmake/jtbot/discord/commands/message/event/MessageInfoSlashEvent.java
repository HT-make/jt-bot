package htmake.jtbot.discord.commands.message.event;

import htmake.jtbot.discord.commands.message.cache.MessageCache;
import htmake.jtbot.discord.commands.message.data.Message;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.domain.message.entity.ScheduledMessage;
import htmake.jtbot.global.cache.CacheFactory;
import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageInfoSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    private final MessageCache messageCache;

    public MessageInfoSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();

        this.messageCache = CacheFactory.messageCache;
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            requestSuccess(event, response.getBody().getObject().getJSONArray("messageScheduleList"));
        } else {
            errorUtil.sendError(event.getHook(), "메시지 목록", "메시지 목록을 불러올 수 없습니다. 잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String userId) {
        String endPoint = "/message/info/{user_id}";
        Pair<String, String> routeParam = new Pair<>("user_id", userId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONArray messageList) {
        List<ScheduledMessage> messages = toMessageList(messageList);

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl())
                .setTitle("예약된 메시지 목록");

        List<Message> messageCacheList = new ArrayList<>();

        for (int i = 0; i < messages.size(); i++) {
            ScheduledMessage message = messages.get(i);
            String messageContent = message.getMessage();
            String truncatedMessage = messageContent.length() > 10 ? messageContent.substring(0, 10) + "..." : messageContent;

            embedBuilder.addField((i + 1) + "번", message.getScheduledTime() + " >> " + truncatedMessage, false);
            embedBuilder.addField("", "", false);

            Message messageToCache = Message.builder()
                    .messageId(message.getId())
                    .buttonId(i + 1)
                    .build();

            messageCacheList.add(messageToCache);
        }

        messageCache.put(event.getUser().getId(), messageCacheList);

        List<Button> buttons = new ArrayList<>();
        if (messages.size() > 0) {
            buttons.add(Button.primary("message-button-cancel", "취소하기"));

            event.replyEmbeds(embedBuilder.build())
                    .addActionRow(buttons)
                    .setEphemeral(true).queue();

        } else {
            embedBuilder.setDescription("예약된 메시지가 없습니다.");
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }

    private List<ScheduledMessage> toMessageList(JSONArray messageList) {
        List<ScheduledMessage> messages = new ArrayList<>();

        for (int i = 0; i < messageList.length(); i++) {
            JSONObject messageObject = messageList.getJSONObject(i);

            ScheduledMessage message = ScheduledMessage.builder()
                    .id(messageObject.getLong("id"))
                    .message(messageObject.getString("message"))
                    .scheduledTime(messageObject.getString("scheduledTime"))
                    .build();
            messages.add(message);
        }

        return messages;
    }
}