package htmake.jtbot.discord.util;


import htmake.jtbot.discord.commands.global.cache.MessageCache;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageUtil {

    private final MessageCache messageCache;

    public MessageUtil() {
        this.messageCache = CacheFactory.messageCache;
    }

    public void put(String playerId) {
        messageCache.put(playerId, true);
    }

    public void remove(String playerId) {
        messageCache.remove(playerId);
    }

    public boolean validCheck(Message message, String name) {
        MessageEmbed embed = message.getEmbeds().get(0);
        return !embed.getAuthor().getName().equals(name);
    }

    public boolean validCheck(String playerId) {
        return messageCache.containsKey(playerId);
    }
}
