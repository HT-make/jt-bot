package htmake.jtbot.domain.gemini.presentation;

import htmake.jtbot.domain.gemini.service.GeminiChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gemini")
@RequiredArgsConstructor
public class GeminiChatController {

    private final GeminiChatService geminiChatService;

    @PostMapping("/chat")
    public ResponseEntity<String> getResponse(@RequestBody @Valid String question) {
        String response = geminiChatService.execute(question);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}