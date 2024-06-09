package htmake.jtbot.global.cache;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    //poll
    public static PollCache pollCache;

    public CacheFactory() {
        pollCache = new PollCache();
    }
}
