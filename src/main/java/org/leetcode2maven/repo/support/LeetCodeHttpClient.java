package org.leetcode2maven.repo.support;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.leetcode2maven.repo.dataobject.graphql.QueryRequest;

import java.util.Map;

public class LeetCodeHttpClient {

    private Gson gson = new Gson();


    public LeetCodeHttpClient() {
        Unirest.config().defaultBaseUrl("https://leetcode.com")
                .setDefaultHeader("Content-Type", "application/json")
                .setDefaultHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
    }

    public <T> T graphQLQuery(String query, Map<String, Object> variables, Class<T> responseClass) {

        QueryRequest payload = new QueryRequest();
        payload.setQuery(query);
        payload.setVariables(variables);

        HttpResponse<String> response = Unirest.post("/graphql").body(payload).asString();
        return parseGraphQLRawResponse(response, query, responseClass);
    }


    public <T> T doGet(String url, Class<T> responseClass) {
        HttpResponse<T> httpResponse = Unirest.get(url).asObject(responseClass);
        return parseApiResponse(httpResponse, url, responseClass);
    }

    private <T> T parseApiResponse(HttpResponse<T> response, String url, Class<T> responseClass) {
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw buildHttpFailureException(url, response);
        }
    }


    private <T> T parseGraphQLRawResponse(HttpResponse<String> response, String query, Class<T> responseClass) {
        if (response.isSuccess()) {
            JsonObject root = gson.fromJson(response.getBody(), JsonObject.class);
            JsonObject data = root.getAsJsonObject("data");
            JsonElement theOnlyChild = data.get(data.keySet().iterator().next());
            return gson.fromJson(theOnlyChild, responseClass);
        } else {
            throw buildHttpFailureException(query, response);
        }
    }


    private static RuntimeException buildHttpFailureException(String glQueryOrApiUrl, HttpResponse<?> response) {
        return new RuntimeException("Failed to invoke http request for " + glQueryOrApiUrl + ". The status code is " + response.getStatus());
    }

}