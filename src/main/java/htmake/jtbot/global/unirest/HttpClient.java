package htmake.jtbot.global.unirest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kotlin.Pair;
import org.springframework.stereotype.Component;

@Component
public interface HttpClient {

    HttpResponse<JsonNode> sendPostRequest(String endpoint, String requestBody);

    HttpResponse<JsonNode> sendPostRequest(String endPoint, Pair<String, String> routeParam, String requestBody);

    HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam);

    HttpResponse<JsonNode> sendDeleteRequest(String endPoint, Pair<String, String> routeParam);
}
