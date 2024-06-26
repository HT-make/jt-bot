package htmake.jtbot.discord.commands.poll.util;

import htmake.jtbot.discord.commands.poll.cache.PollCache;
import htmake.jtbot.global.cache.CacheFactory;
import kotlin.Pair;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class PollUtil {

    private final PollCache pollCache;

    public PollUtil() {
        this.pollCache = CacheFactory.pollCache;
    }

    public String formatTitle(int number, String option) {
        String emoji = "";

        switch (number) {
            case 1 -> emoji = ":one:";
            case 2 -> emoji = ":two:";
            case 3 -> emoji = ":three:";
            case 4 -> emoji = ":four:";
            case 5 -> emoji = ":five:";
            case 6 -> emoji = ":six:";
            case 7 -> emoji = ":seven:";
            case 8 -> emoji = ":eight:";
            case 9 -> emoji = ":nine:";
            case 10 -> emoji = ":keycap_ten:";
        }

        return emoji + " " + option;
    }

    public String getUnicode(int number) {
        return switch (number) {
            case 1 -> "1️⃣";
            case 2 -> "2️⃣";
            case 3 -> "3️⃣";
            case 4 -> "4️⃣";
            case 5 -> "5️⃣";
            case 6 -> "6️⃣";
            case 7 -> "7️⃣";
            case 8 -> "8️⃣";
            case 9 -> "9️⃣";
            case 10 -> "\uD83D\uDD1F";
            default -> null;
        };
    }

    public void setActionRowList(List<ActionRow> actionRowList, List<Button> buttonList) {
        List<Button> currentRowButtonList = new ArrayList<>();

        for (Button button : buttonList) {
            currentRowButtonList.add(button);

            if (currentRowButtonList.size() == 5) {
                actionRowList.add(ActionRow.of(currentRowButtonList));
                currentRowButtonList = new ArrayList<>();
            }
        }

        if (!currentRowButtonList.isEmpty()) {
            actionRowList.add(ActionRow.of(currentRowButtonList));
        }
    }

    public Pair<Boolean, String> pollValidCheck(int pollId, String userId) {
        if (!pollCache.containsKey(pollId)) {
            return new Pair<>(false, "투표를 찾을 수 없습니다.");
        }

        String author = pollCache.get(pollId).getAuthor();

        if (!author.equals(userId)) {
            return new Pair<>(false, "권한이 없습니다.");
        }

        return new Pair<>(true, "Valid");
    }
}
