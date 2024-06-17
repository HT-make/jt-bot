package htmake.jtbot.domain.message.service.impl;

import htmake.jtbot.domain.message.entity.ScheduledMessage;
import htmake.jtbot.domain.message.presentation.data.response.MessageScheduleListResponse;
import htmake.jtbot.domain.message.presentation.data.response.MessageScheduleResponse;
import htmake.jtbot.domain.message.repository.MessageRepository;
import htmake.jtbot.domain.message.service.ScheduledMessageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduledMessageInfoServiceImpl implements ScheduledMessageInfoService {

    private final MessageRepository messageRepository;

    @Override
    public MessageScheduleListResponse execute(String userId) {
        List<ScheduledMessage> messageScheduleList = messageRepository.findByUserId(userId);

        return MessageScheduleListResponse.builder()
                .messageScheduleList(
                        messageScheduleList.stream()
                                .map(MessageScheduleResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
