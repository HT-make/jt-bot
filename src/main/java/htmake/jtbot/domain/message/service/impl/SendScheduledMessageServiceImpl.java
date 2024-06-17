package htmake.jtbot.domain.message.service.impl;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageListResponse;
import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageResponse;
import htmake.jtbot.domain.message.repository.MessageRepository;
import htmake.jtbot.domain.message.service.SendScheduledMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SendScheduledMessageServiceImpl implements SendScheduledMessageService {

    private final MessageRepository messageRepository;

    @Override
    public ScheduledMessageListResponse execute() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String time = now.format(formatter);

        List<ScheduledMessage> messageScheduleList = messageRepository.findByScheduledTime(time);

        if (messageScheduleList.isEmpty()) {
            return null;
        }

        ScheduledMessageListResponse response = ScheduledMessageListResponse.builder()
                .scheduledMessageList(
                        messageScheduleList.stream()
                                .map(ScheduledMessageResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();

        messageRepository.deleteAll(messageScheduleList);

        return response;
    }
}
