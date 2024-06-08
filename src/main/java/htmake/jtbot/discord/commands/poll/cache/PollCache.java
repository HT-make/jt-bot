package htmake.jtbot.discord.commands.poll.cache;

import htmake.jtbot.discord.commands.poll.data.Poll;
import htmake.jtbot.global.cache.CacheManager;

public class PollCache {

    private final CacheManager<Integer, Poll> cache;

    private int index;

    public PollCache() {
        this.cache = new CacheManager<>();
        this.index = 1;
    }

    public void put(Integer key, Poll poll) {
        cache.put(key, poll);
    }

    public Poll get(Integer key) {
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
