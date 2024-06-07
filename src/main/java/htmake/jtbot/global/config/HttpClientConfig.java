package htmake.jtbot.global.config;

import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient() {
        return new HttpClientImpl();
    }
}
