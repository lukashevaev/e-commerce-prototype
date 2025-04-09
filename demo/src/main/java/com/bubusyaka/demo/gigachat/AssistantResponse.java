package com.bubusyaka.demo.gigachat;

import com.bubusyaka.demo.configuration.GigaChatSettings;
import com.bubusyaka.demo.configuration.GigachatConfig;
import com.bubusyaka.demo.service.DataSetOfUsersAndItemsService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AssistantResponse {
    private static final Gson gson = new Gson();

    private final DataSetOfUsersAndItemsService dataSetOfUsersAndItemsService;
    private final RestClient restClient;

    @Autowired
    private GigachatConfig gigachatConfig;

    public String getAssistantResponse(String accessToken, String userMessage) throws Exception {
        String bodyContent = "{"
                + "  \"model\": \"" + GigaChatSettings.MODEL + "\","
                + "  \"messages\": ["
                + "    {"
                + "      \"role\": \"user\","
                + "      \"content\": \"" + userMessage + "\""
                + "    }"
                + "  ],"
                + "  \"temperature\": " + GigaChatSettings.TEMPERATURE + ","
                + "  \"top_p\": " + GigaChatSettings.TOP_P + ","
                + "  \"n\": " + GigaChatSettings.N + ","
                + "  \"stream\": false,"
                + "  \"max_tokens\": " + GigaChatSettings.MAX_TOKENS + ","
                + "  \"repetition_penalty\": " + GigaChatSettings.REPETITION_PENALTY
                + "}";

        RequestBody body = RequestBody.create(bodyContent, MediaType.parse(gigachatConfig.getMediaTypeJson()));

//        Request request = new Request.Builder()
//                .url(gigachatConfig.getChatURL())
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .build();

        ResponseEntity<String> response = restClient.post()
                .uri(gigachatConfig.getChatURL())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .toEntity(String.class);


        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody().toString();
            return extractAssistantResponse(responseBody);
        } else {
            throw new Exception("Не удалось получить ответ помощника. Код ответа: " + response.getStatusCode());
        }

//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                String responseBody = response.body().string();
//                return extractAssistantResponse(responseBody);
//            } else {
//                throw new Exception("Не удалось получить ответ помощника. Код ответа: " + response.code());
//            }
//        }
    }

    private static String extractAssistantResponse(String responseBody) {
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
}
