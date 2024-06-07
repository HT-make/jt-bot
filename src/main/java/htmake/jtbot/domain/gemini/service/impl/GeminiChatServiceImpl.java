package htmake.jtbot.domain.gemini.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import htmake.jtbot.domain.gemini.service.GeminiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiChatServiceImpl implements GeminiChatService {

    private final RestTemplate restTemplate;

    @Value("${google.gemini.api.url}")
    private String url;

    @Value("${google.gemini.api.key}")
    private String key;

    public String execute(String question) {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode requestBodyNode = objectMapper.createObjectNode()
                .set("contents", objectMapper.createArrayNode().add(
                        objectMapper.createObjectNode().set("parts", objectMapper.createArrayNode().add(
                                objectMapper.createObjectNode().put("text", question)
                        ))
                ));

        try {
            String requestBody = objectMapper.writeValueAsString(requestBodyNode);
            return restTemplate.postForObject(url + key, requestBody, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to construct JSON request body", e);
        }
    }
}
