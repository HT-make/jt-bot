package htmake.jtbot.global.cache;

import htmake.jtbot.discord.commands.global.cache.MessageCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    //global
    public static MessageCache messageCache;

    public CacheFactory() {
        messageCache = new MessageCache();
    }
}
