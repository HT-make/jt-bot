package htmake.jtbot.global.unirest.impl;

import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.util.RestServiceType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kotlin.Pair;

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

    @Override
    public HttpResponse<JsonNode> sendPostRequest(String endPoint, Pair<String, String> routeParam, String requestBody) {
        try {
            return Unirest.post(RestServiceType.DEFAULT_URL + endPoint)
                    .header("Content-Type", "application/json")
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .body(requestBody)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP POST request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam) {
        try {
            return Unirest.get(RestServiceType.DEFAULT_URL + endPoint)
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP GET request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendDeleteRequest(String endPoint, Pair<String, String> routeParam) {
        try {
            return Unirest.delete(RestServiceType.DEFAULT_URL + endPoint)
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP DELETE request", e);
        }
    }
}
