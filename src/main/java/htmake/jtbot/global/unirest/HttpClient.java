package htmake.jtbot.global.unirest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.springframework.stereotype.Component;

@Component
public interface HttpClient {

    HttpResponse<JsonNode> sendPostRequest(String endpoint, String requestBody);
}
