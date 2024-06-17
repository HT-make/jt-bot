package htmake.jtbot.domain.message.service.impl;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import htmake.jtbot.domain.message.exception.MaxMessageSizeExceededException;
import htmake.jtbot.domain.message.presentation.data.request.MessageScheduleRequest;
import htmake.jtbot.domain.message.repository.MessageRepository;
import htmake.jtbot.domain.message.service.MessageScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageScheduleServiceImpl implements MessageScheduleService {

    private final MessageRepository messageRepository;

    @Override
    public void execute(String userId, MessageScheduleRequest request) {
        int messageCount = messageRepository.countByUserId(userId);
        if (messageCount >= 5) {
            throw new MaxMessageSizeExceededException();
        }

        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .userId(userId)
                .userName(request.getUserName())
                .userAvatar(request.getUserAvatar())
                .guildId(request.getGuildId())
                .channelId(request.getChannelId())
                .message(request.getMessage())
                .scheduledTime(request.getScheduledTime())
                .build();

        messageRepository.save(scheduledMessage);
    }
}
