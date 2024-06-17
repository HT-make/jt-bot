package htmake.jtbot.domain.message.exception;

import htmake.jtbot.global.error.BasicException;
import htmake.jtbot.global.error.ErrorCode;

public class ScheduledMessageNotFoundException extends BasicException {
    public ScheduledMessageNotFoundException() {
        super(ErrorCode.SCHEDULED_MESSAGE_NOT_FOUND);
    }
}
