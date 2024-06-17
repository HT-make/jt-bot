package htmake.jtbot.global.cache;

import htmake.jtbot.discord.commands.gemini.cache.GeminiChatCache;
import htmake.jtbot.discord.commands.message.cache.MessageCache;
import htmake.jtbot.discord.commands.poll.cache.PollCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    //poll
    public static PollCache pollCache;

    //gemini
    public static GeminiChatCache geminiChatCache;

    //message
    public static MessageCache messageCache;

    public CacheFactory() {
        pollCache = new PollCache();
        geminiChatCache = new GeminiChatCache();
        messageCache = new MessageCache();
    }
}
