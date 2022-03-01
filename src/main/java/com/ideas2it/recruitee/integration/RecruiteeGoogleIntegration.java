package com.ideas2it.recruitee.integration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ideas2it.recruitee.integration.dto.Event;

public class RecruiteeGoogleIntegration implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Processing request : " + context.getAwsRequestId());
        logger.log("Input : " + input.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Event event;
        try {
            event = objectMapper.readValue(input.getBody(), Event.class);
            logger.log("Event : " + objectMapper.writer().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            logger.log(e.getMessage());
        }
        String trackingId = System.getenv("TRACKING_ID");
        HttpClient client = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("www.google-analytics.com")
                .setPath("/collect")
                .addParameter("v", "1") // API Version.
                .addParameter("tid", trackingId)
                .addParameter("cid", String.valueOf(new Random().nextLong()))
                .addParameter("t", "event") // Event hit type.
                .addParameter("ec", "Hiring Process") // Event category.
                .addParameter("ea", "Applied") // Event action.
                .addParameter("ua", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                .addParameter("el", "Applied")
                .addParameter("cs", "googleads.g.doubleclick.net")
                .addParameter("cm", "referer")
                .addParameter("uip", input.getRequestContext().getHttp().getSourceIp());
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            logger.log(e.getMessage());
        }
        HttpPost request = new HttpPost(uri);
        try {
            HttpResponse httpResponse = client.execute(request);
            logger.log("Status: " + httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            logger.log("Error: " + e.getMessage());
        }
        return APIGatewayV2HTTPResponse.builder().withStatusCode(200).build();
    }
}
