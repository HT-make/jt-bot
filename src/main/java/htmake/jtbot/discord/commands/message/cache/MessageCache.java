package htmake.jtbot.discord.commands.message.cache;

import htmake.jtbot.discord.commands.message.data.Message;
import htmake.jtbot.global.cache.CacheManager;

import java.util.List;

public class MessageCache {
    private final CacheManager<String, List<Message>> cache;

    public MessageCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, List<Message> messageList) {
        cache.put(key, messageList);
    }

    public List<Message> get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
