package htmake.jtbot.domain.message.service;

import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageListResponse;

public interface SendScheduledMessageService {
    ScheduledMessageListResponse execute();
}
