package htmake.jtbot.discord.commands.message.event;

import htmake.jtbot.discord.commands.message.cache.MessageCache;
import htmake.jtbot.discord.commands.message.data.Message;
import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class MessageCancelPageButtonEvent {

    private final ErrorUtil errorUtil;
    private final MessageCache messageCache;

    public MessageCancelPageButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.messageCache = CacheFactory.messageCache;
    }

    public void execute(ButtonInteractionEvent event) {
        if (!messageCache.containsKey(event.getUser().getId())){
            errorUtil.sendError(event.getHook(), "메시지", "메시지를 찾을 수 없습니다.");
            return;
        }

        List<Message> messageList = messageCache.get(event.getUser().getId());
        List<Button> buttonList = new ArrayList<>();

        for (Message message : messageList) {
            Button button = Button.primary("message-cancel-" + message.getMessageId(), message.getButtonId() + "번");
            buttonList.add(button);
        }

        List<ActionRow> actionRows = new ArrayList<>();
        actionRows.add(ActionRow.of(buttonList));

        event.getHook().editOriginalComponents(actionRows).queue();
    }
}
