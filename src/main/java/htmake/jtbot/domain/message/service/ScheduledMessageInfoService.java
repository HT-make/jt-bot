package htmake.jtbot.domain.message.service;

import htmake.jtbot.domain.message.presentation.data.response.MessageScheduleListResponse;

public interface ScheduledMessageInfoService {
    MessageScheduleListResponse execute(String userId);
}
