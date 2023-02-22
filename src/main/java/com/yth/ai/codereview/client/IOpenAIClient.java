package com.yth.ai.codereview.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface IOpenAIClient {
    @Headers({"Content-Type: application/json", "Authorization: Bearer {secretKey}"})
    @RequestLine("POST /v1/completions")
    OpenAIResponse request(@Param("secretKey") String secretKey, OpenAIRequest requisicao);
}
