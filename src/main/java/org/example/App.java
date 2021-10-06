package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Основной мэин класс
 */

public class App {

    public static final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = createHttpClient();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        CloseableHttpResponse response = httpClient.execute(request);

        //Преобразование json в список java объектов
        List<Response> posts = mapper.readValue(
                response.getEntity().getContent(), new TypeReference<List<Response>>() {
                });
        posts.stream()
                //Фильтрация списка (upvotes > 0 и type - "Кошка")
                .filter(value -> value.getUpvotes() > 0 && "cat".equals(value.getType()))
                //вывод на экран
                .forEach(System.out::println);
    }

    /**
     * Метод создаёт httpClient
     */
    private static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
    }
}
