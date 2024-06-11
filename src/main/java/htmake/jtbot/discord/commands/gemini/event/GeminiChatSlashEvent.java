package htmake.jtbot.discord.commands.gemini.event;

import htmake.jtbot.discord.commands.gemini.cache.GeminiChatCache;
import htmake.jtbot.discord.commands.gemini.data.Chat;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.discord.util.ObjectMapperUtil;
import htmake.jtbot.global.cache.CacheFactory;
import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeminiChatSlashEvent {

    private final HttpClient httpClient;

    private final ObjectMapperUtil objectMapperUtil;
    private final ErrorUtil errorUtil;

    private final GeminiChatCache geminiChatCache;

    public GeminiChatSlashEvent() {
        this.httpClient = new HttpClientImpl();

        this.objectMapperUtil = new ObjectMapperUtil();
        this.errorUtil = new ErrorUtil();

        this.geminiChatCache = CacheFactory.geminiChatCache;
    }

    public void execute(SlashCommandInteractionEvent event) {
        String question = event.getOption("질문").getAsString();

        event.deferReply().queue();

        HttpResponse<JsonNode> response = request(question);

        if (response.getStatus() == 200) {
            String text = response.getBody().getObject()
                    .getJSONArray("candidates").getJSONObject(0)
                    .getJSONObject("content").getJSONArray("parts").getJSONObject(0)
                    .getString("text");

            requestSuccess(event, text, question);
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

    private void requestSuccess(SlashCommandInteractionEvent event, String answer, String question) {
        int answerLength = answer.length();
        int questionLength = question.length();
        int size = answerLength + questionLength;

        int maxPage = (size % 1000 == 0 ? size / 1000 : size / 1000 + 1);

        int endIndex = Math.min(1000 - questionLength, answerLength);
        saveChatCache(question, answer, maxPage);

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .addField(question, "", false)
                .addField("", answer.substring(0, endIndex), false)
                .build();

        if (size > 1000){
            List<Button> buttonList = buttonEmbed(maxPage);
            event.getHook().sendMessageEmbeds(embed).addActionRow(buttonList).queue();
        } else {
            event.getHook().sendMessageEmbeds(embed).queue();
        }

        geminiChatCache.setIndex(geminiChatCache.getIndex() + 1);
    }

    public List<Button> buttonEmbed(int page) {
        List<Button> buttonList = new ArrayList<>();
        int index = geminiChatCache.getIndex();

        Button leftButton = Button.primary("gemini-chat-" + index + "-0", "◄").asDisabled();
        Button pageButton = Button.secondary("blank", "1/" + page).asDisabled();
        Button rightButton = Button.primary("gemini-chat-" + index + "-2", "►");

        buttonList.add(leftButton);
        buttonList.add(pageButton);
        buttonList.add(rightButton);

        return buttonList;
    }

    private void saveChatCache(String question, String answer, int maxPage){
        List<String> answerList = toAnswerList(answer, question.length());

        Chat chat = Chat.builder()
                .question(question)
                .answerList(answerList)
                .maxPage(maxPage)
                .build();

        int index = geminiChatCache.getIndex();
        geminiChatCache.put(index, chat);
    }

    private List<String> toAnswerList(String answer, int questionLength) {
        List<String> answerList = new ArrayList<>();
        int answerLength = answer.length();

        int firstElementLength = Math.min(1000 - questionLength, answerLength);
        answerList.add(answer.substring(0, firstElementLength));

        for (int i = firstElementLength; i < answerLength; i += 1000) {
            int end = Math.min(i + 1000, answerLength);
            answerList.add(answer.substring(i, end));
        }

        return answerList;
    }
}
