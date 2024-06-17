package htmake.jtbot.domain.message.exception;

import htmake.jtbot.global.error.BasicException;
import htmake.jtbot.global.error.ErrorCode;

public class MaxMessageSizeExceededException extends BasicException {

    public MaxMessageSizeExceededException() {
        super(ErrorCode.MAX_MESSAGE_SIZE_EXCEEDED);
    }
}
