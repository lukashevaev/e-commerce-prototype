package com.bubusyaka.demo.service;

import com.bubusyaka.demo.configuration.GigaChatSettings;
import com.bubusyaka.demo.configuration.GigachatConfig;
import com.bubusyaka.demo.gigachat.GigaChatRequest;
import com.bubusyaka.demo.gigachat.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GigaChatDialog {
    //private final DataSetOfUsersAndItemsService dataSetOfUsersAndItemsService;
    private final TokenProvider tokenProvider;
    private final GigachatConfig gigachatConfig;
    private static final Gson gson = new Gson();
    private final RestClient secureRestClient;

    public String getResponse(String message) throws Exception {
        String accessToken = tokenProvider.getAccessToken();

        String bodyContent = "{" +
                "\"model\": \"" + GigaChatSettings.MODEL + "\"," +
                "\"messages\": [" +
                    "{" +
                        "\"role\": \"user\"," +
                        "\"content\": \"" + message + "\"" +
                    "}" +
                "]," +
                "\"temperature\": " + GigaChatSettings.TEMPERATURE + "," +
                "\"top_p\": " + GigaChatSettings.TOP_P + "," +
                "\"n\": " + GigaChatSettings.N + "," +
                "\"stream\": false," +
                "\"max_tokens\": " + GigaChatSettings.MAX_TOKENS + "," +
                "\"repetition_penalty\": " + GigaChatSettings.REPETITION_PENALTY +
                "}";

        ResponseEntity<Object> response = secureRestClient.post()
                .uri(gigachatConfig.getChatURL())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(bodyContent)
                .retrieve()
                .toEntity(Object.class);
//        System.out.println("response status code is: " + response.getStatusCode());
        System.out.println("request body is: " + bodyContent);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Получаем тело ответа
            Object body = response.getBody();
            // Преобразуем тело ответа в JSON-строку
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody;
            jsonBody = mapper.writeValueAsString(body);
            String result = extractAssistantResponse(jsonBody);
            return result.substring(7, result.length()-3);
        }
        else {
            throw new Exception("Не удалось получить ответ помощника. Код ответа: " + response.getStatusCode());
        }
    }

    public String extractAssistantResponse(String responseBody) {

        JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class);
        JsonArray choicesArray = jsonObj.getAsJsonArray("choices");
        if (choicesArray != null && !choicesArray.isEmpty()) {
            JsonObject choiceObj = choicesArray.get(0).getAsJsonObject();
            JsonObject messageObj = choiceObj.getAsJsonObject("message");
            return messageObj.has("content") ? messageObj.get("content").getAsString() : "";
        } else {
            throw new IllegalStateException("Массив 'choices' имеет значение null или пустой");
        }
    }

    public List<String> responseParser(String responseBody) throws JsonProcessingException {
        // Создание объекта ObjectMapper для работы с JSON
        ObjectMapper mapper = new ObjectMapper();
        // Парсинг JSON-строки в объект JsonNode
        JsonNode rootNode = mapper.readTree(responseBody);
        // Извлечение списка категорий
        JsonNode categoriesNode = rootNode.get("Categories");
        // Преобразование узла в List<String>
        List<String> categories = new ArrayList<>();
        for (JsonNode category : categoriesNode) {
            categories.add(category.asText());
        }
        return categories;
    }


}
