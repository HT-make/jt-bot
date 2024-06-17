package htmake.jtbot.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MAX_MESSAGE_SIZE_EXCEEDED("메시지 예약 가능 갯수를 모두 사용하였습니다.", 403),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", 404),
    SCHEDULED_MESSAGE_NOT_FOUND("예약된 메시지를 찾을 수 없습니다.", 404),
    ;

    private final String message;
    private final int status;
}
