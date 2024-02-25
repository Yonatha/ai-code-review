package com.yth.ai.codereview.client.GoogleAI;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface IGeminiAIClient {
    @Headers({"Content-Type: application/json"})
    @RequestLine("POST /models/{model}:generateContent?key={apiKey}")
    GeminiAIResponse request(
            @Param("model") String model,
            @Param("apiKey") String apiKey,
            GeminiAIRequest request
    );
}
