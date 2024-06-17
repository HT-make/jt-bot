package htmake.jtbot.domain.message.service;

import htmake.jtbot.domain.message.presentation.data.request.MessageScheduleRequest;

public interface MessageScheduleService {
    void execute(String userId, MessageScheduleRequest request);
}
