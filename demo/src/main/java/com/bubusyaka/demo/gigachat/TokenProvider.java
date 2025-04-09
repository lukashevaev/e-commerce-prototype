package com.bubusyaka.demo.gigachat;

import com.bubusyaka.demo.configuration.GigachatConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private static final String RQ_UID = UUID.randomUUID().toString(); // Генерация случайного UUID для RqUID
    private final RestClient secureRestClient;
    private final RestClient restClient;
    //private static final OkHttpClient client = new OkHttpClient().newBuilder().build(); // Создание клиента HTTP
    private static final Gson gson = new Gson(); // Создание экземпляра Gson для работы с JSON
    private static String accessToken; // Токен доступа
    private static long tokenExpirationTime; // Время истечения токена
    @Autowired
    private GigachatConfig gigachatConfig;

    public String getAccessToken() throws Exception {
        long currentTime = System.currentTimeMillis(); // Текущее время в миллисекундах
        // Если токен еще не получен или истекло его время действия, обновляем токен
        if (accessToken == null || currentTime > tokenExpirationTime) {
            updateToken();
        }
        return accessToken; // Возвращаем токен доступа
    }


    private void updateToken() throws Exception {
        String authData = Base64.getEncoder().encodeToString((gigachatConfig.getClientId() + ":" + gigachatConfig.getClientSecret()).getBytes()); // Кодирование данных авторизации в Base64
        String authHeader = "Basic " + authData; // Формирование заголовка авторизации

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("scope", "GIGACHAT_API_PERS");

        ResponseEntity<String> response = secureRestClient.post()
                .uri(gigachatConfig.getOauthURL())
                .body(map)
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .header("Accept", "application/json")
                .header("RqUID", UUID.randomUUID().toString())
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody().toString();
            accessToken = extractAccessToken(responseBody);
            //Устанавливаем время истечения токена на 30 минут в будущем
            tokenExpirationTime = System.currentTimeMillis() + 30 * 60 * 1000;
        } else {
            throw new Exception("Не удалось получить токен доступа. Код ответа: " + response.getStatusCode());
        }

//        Request request = new Request.Builder() // Создание запроса
//                .url(gigachatConfig.getOauthURL())
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Accept", "application/json")
//                .addHeader("RqUID", UUID.randomUUID().toString()) // Генерация идентификатора запроса
//                .addHeader("Authorization", authHeader)
//                .build();
//
//        try (Response response = secureRestClient.newCall(request).execute()) { // Выполнение запроса
//            if (response.isSuccessful() && response.body() != null) { // Если запрос успешен и тело ответа не пустое
//                String responseBody = response.body().string(); // Получение тела ответа
//                accessToken = extractAccessToken(responseBody); // Извлечение токена доступа из тела ответа
//                // Устанавливаем время истечения токена на 30 минут в будущем
//                tokenExpirationTime = System.currentTimeMillis() + 30 * 60 * 1000;
//            } else {
//                throw new Exception("Не удалось получить токен доступа. Код ответа: " + response.code()); // Если запрос неуспешен, выбрасываем исключение
//            }
//        }
    }

    private String extractAccessToken(String responseBody) {
        JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class); // Преобразование строки ответа в объект JSON
        return jsonObj.get("access_token").getAsString(); // Возвращаем значение токена доступа
    }
}
