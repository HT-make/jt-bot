package htmake.jtbot.discord.commands.gemini.event;

import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.discord.util.ObjectMapperUtil;
import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GeminiChatSlashCommand {

    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;
    private final ErrorUtil errorUtil;

    public GeminiChatSlashCommand() {
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        String question = event.getOption("질문").getAsString();

        HttpResponse<JsonNode> response = request(question);
        System.out.println(response.getBody().getObject().toString());

        String text = response.getBody().getObject()
                .getJSONArray("candidates").getJSONObject(0)
                .getJSONObject("content").getJSONArray("parts").getJSONObject(0)
                .getString("text");

        if (response.getStatus() == 200) {
            requestSuccess(event, text);
        } else {
            errorUtil.sendError(event.getHook(), "호떡이", "호떡이를 이용할 수 없습니다. 잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String question){
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("question", question);

        String endPoint = "/gemini/chat";
        String jsonBody = objectMapperUtil.mapper(requestData);
        return httpClient.sendPostRequest(endPoint, jsonBody);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, String answer) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription(answer)
                .build();

        event.replyEmbeds(embed).queue();
    }
}
