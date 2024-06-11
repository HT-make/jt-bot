package htmake.jtbot.global.unirest.impl;

import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.util.RestServiceType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class HttpClientImpl implements HttpClient {

    public HttpClientImpl() {
        Unirest.config()
                .socketTimeout(20000)
                .connectTimeout(20000);
    }

    @Override
    public HttpResponse<JsonNode> sendPostRequest(String endPoint, String requestBody) {
        try {
            return Unirest.post(RestServiceType.DEFAULT_URL + endPoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP POST request", e);
        }
    }
}
