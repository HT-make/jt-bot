package htmake.jtbot.discord.commands.message.event;

import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;

public class MessageCancelButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public MessageCancelButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, String messageId) {

        HttpResponse<JsonNode> response = request(messageId);

        if (response.getStatus() == 200) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle("메세지 취소")
                    .setDescription("메세지가 취소되었습니다.")
                    .build();

            event.getHook().editOriginalComponents(Collections.emptyList()).queue();
            event.getHook().editOriginalEmbeds(embed).queue();
        } else {
            errorUtil.sendError(event.getHook(), "메시지 취소", "예약된 메시지를 취소할 수 없습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String messageId) {
        String endPoint = "/message/cancel/{message_id}";
        Pair<String, String> routeParam = new Pair<>("message_id", messageId);
        return httpClient.sendDeleteRequest(endPoint, routeParam);
    }
}
