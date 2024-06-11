package htmake.jtbot.discord.commands.gemini.cache;

import htmake.jtbot.discord.commands.gemini.data.Chat;
import htmake.jtbot.global.cache.CacheManager;

public class GeminiChatCache {

    private final CacheManager<Integer, Chat> cache;

    private int index;

    public GeminiChatCache() {
        this.cache = new CacheManager<>();
        this.index = 1;
    }

    public void put(Integer key, Chat chat) {
        cache.put(key, chat);
    }

    public Chat get(Integer key) {
        return cache.get(key);
    }

    public boolean containsKey(Integer key) {
        return cache.containsKey(key);
    }

    public void remove(Integer key) {
        cache.remove(key);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
