package htmake.jtbot.global.scheduler;

import htmake.jtbot.discord.commands.message.event.SendScheduledMessageEvent;
import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageListResponse;
import htmake.jtbot.domain.message.service.SendScheduledMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final SendScheduledMessageService sendScheduledMessageService;
    private final SendScheduledMessageEvent sendScheduledMessageEvent;

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        ScheduledMessageListResponse response = sendScheduledMessageService.execute();
        if (response == null) {
            return;
        }

        sendScheduledMessageEvent.execute(response.getScheduledMessageList());
    }
}
