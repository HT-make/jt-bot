package htmake.jtbot.domain.message.presentation;

import htmake.jtbot.domain.message.presentation.data.request.MessageScheduleRequest;
import htmake.jtbot.domain.message.presentation.data.response.MessageScheduleListResponse;
import htmake.jtbot.domain.message.presentation.data.response.ScheduledMessageListResponse;
import htmake.jtbot.domain.message.service.MessageScheduleService;
import htmake.jtbot.domain.message.service.MessageCancelService;
import htmake.jtbot.domain.message.service.ScheduledMessageInfoService;
import htmake.jtbot.domain.message.service.SendScheduledMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final ScheduledMessageInfoService messageInfoService;
    private final MessageScheduleService messageScheduleService;
    private final MessageCancelService messageCancelService;
    private final SendScheduledMessageService sendScheduledMessageService;

    @GetMapping("/info/{user_id}")
    public ResponseEntity<MessageScheduleListResponse> messageListInfo(@PathVariable("user_id") String userId) {
        MessageScheduleListResponse response = messageInfoService.execute(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/schedule/{user_id}")
    public ResponseEntity<Void> messageSchedule(
            @PathVariable("user_id") String userId,
            @RequestBody MessageScheduleRequest request
    ) {
        messageScheduleService.execute(userId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{message_id}")
    public ResponseEntity<Void> messageCancel(@PathVariable("message_id") String messageId) {
        messageCancelService.execute(Long.parseLong(messageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/scheduled")
    public ResponseEntity<ScheduledMessageListResponse> sendScheduledMessage() {
        ScheduledMessageListResponse response = sendScheduledMessageService.execute();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
