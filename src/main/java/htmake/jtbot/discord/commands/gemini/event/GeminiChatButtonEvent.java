package htmake.jtbot.discord.commands.gemini.event;

import htmake.jtbot.discord.commands.gemini.cache.GeminiChatCache;
import htmake.jtbot.discord.commands.gemini.data.Chat;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GeminiChatButtonEvent {

    private final ErrorUtil errorUtil;

    private final GeminiChatCache geminiChatCache;

    public GeminiChatButtonEvent() {
        this.errorUtil = new ErrorUtil();

        this.geminiChatCache = CacheFactory.geminiChatCache;
    }

    public void execute(ButtonInteractionEvent event, int chatId, int page) {
        if (!geminiChatCache.containsKey(chatId)){
            errorUtil.sendError(event.getHook(), "호떡이", "대화 내용을 찾을 수 없습니다.");
            return;
        }

        Chat chat = geminiChatCache.get(chatId);

        MessageEmbed newEmbed = buildEmbed(page, chat);

        int maxPage = chat.getMaxPage();
        List<Button> buttonList = buttonEmbed(page, maxPage, chatId);

        event.getHook().editOriginalEmbeds(newEmbed)
                .setActionRow(buttonList)
                .queue();
    }

    private MessageEmbed buildEmbed(int page, Chat chat) {
        List<String> answerList = chat.getAnswerList();
        String question = chat.getQuestion();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN);

        if (page == 1) {
            embedBuilder.addField(question, "", false);
            embedBuilder.addField("", answerList.get(0), false);
        } else if (page <= answerList.size()) {
            embedBuilder.addField("", answerList.get(page - 1), false);
        }

        return embedBuilder.build();
    }

    public List<Button> buttonEmbed(int page, int maxPage, int chatId) {
        List<Button> buttonList = new ArrayList<>();

        Button leftButton = Button.primary("gemini-chat-" + chatId + "-" + (page - 1), "◄");
        Button pageButton = Button.secondary("blank", page + "/" + maxPage).asDisabled();
        Button rightButton = Button.primary("gemini-chat-" + chatId + "-" + (page + 1), "►");

        if (page == 1) {
            buttonList.add(leftButton.asDisabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton);
        } else if (page == maxPage) {
            buttonList.add(leftButton);
            buttonList.add(pageButton);
            buttonList.add(rightButton.asDisabled());
        } else {
            buttonList.add(leftButton.asEnabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton.asEnabled());
        }

        return buttonList;
    }
}
