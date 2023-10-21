package com.dattebayo.dattebayo.service;

import com.dattebayo.dattebayo.api.LeetCodeStatsResponse;
import com.dattebayo.dattebayo.util.DattebayoClient;
import com.dattebayo.dattebayo.util.JsonConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@Service
public class LeetCodeStatsService {

    public LeetCodeStatsResponse getStats(String username) {
        String response = "";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("referer", String.format("https://leetcode.com/%s/", username));
        headerMap.put("Content-Type", "application/json");
        DattebayoClient client = new DattebayoClient("https://leetcode.com", null, headerMap);
        String body = String.format("{\"query\":\"query getUserProfile($username: String!) { allQuestionsCount { difficulty count } matchedUser(username: $username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}", username);
        try {
            response = client.makeHttpRequest("graphql", HttpMethod.POST, body, headerMap);
            return getLeetCodeStatsResponseFromJson(JsonConverter.getJsonNode(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LeetCodeStatsResponse getLeetCodeStatsResponseFromJson(JsonNode json) {
        int totalSolved = 0;
        int totalQuestions = 0;
        int easySolved = 0;
        int totalEasy = 0;
        int mediumSolved = 0;
        int totalMedium = 0;
        int hardSolved = 0;
        int totalHard = 0;
        float acceptanceRate = 0;
        int ranking = 0;
        int contributionPoints = 0;
        int reputation = 0;

        final Map<String, Integer> submissionCalendar = new TreeMap<>();
        try {
            JsonNode data = json.get("data");
            ArrayNode allQuestions = (ArrayNode) data.get("allQuestionsCount");
            JsonNode matchedUser = data.get("matchedUser");
            JsonNode submitStats = matchedUser.get("submitStats");
            ArrayNode actualSubmissions = (ArrayNode) submitStats.get("acSubmissionNum");
            ArrayNode totalSubmissions = (ArrayNode) submitStats.get("totalSubmissionNum");

            // Fill in total counts
            totalQuestions = Integer.parseInt(allQuestions.get(0).get("count").asText());
            totalEasy = Integer.parseInt(allQuestions.get(1).get("count").asText());
            totalMedium = Integer.parseInt(allQuestions.get(1).get("count").asText());
            totalHard = Integer.parseInt(allQuestions.get(1).get("count").asText());

            // Fill in solved counts
            totalSolved = Integer.parseInt(actualSubmissions.get(0).get("count").asText());
            easySolved = Integer.parseInt(actualSubmissions.get(1).get("count").asText());
            mediumSolved = Integer.parseInt(actualSubmissions.get(2).get("count").asText());
            hardSolved = Integer.parseInt(actualSubmissions.get(3).get("count").asText());

            // Fill in etc
            float totalAcceptCount = Integer.parseInt(actualSubmissions.get(0).get("submissions").asText());
            float totalSubCount = Integer.parseInt(totalSubmissions.get(0).get("submissions").asText());
            if (totalSubCount != 0) {
                acceptanceRate = round((totalAcceptCount / totalSubCount) * 100, 2);
            }

            contributionPoints = matchedUser.get("contributions").get("points").asInt();
            reputation = matchedUser.get("profile").get("reputation").asInt();
            ranking = matchedUser.get("profile").get("ranking").asInt();

            JsonNode submissionCalendarNode = JsonConverter.getJsonNode(matchedUser.get("submissionCalendar").asText());

            Iterator<Map.Entry<String, JsonNode>> fields = submissionCalendarNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                submissionCalendar.put(field.getKey(), field.getValue().asInt());
            }

        } catch (Exception ex) {
            return LeetCodeStatsResponse.error("error", ex.getMessage());
        }

        return new LeetCodeStatsResponse("success", "retrieved", totalSolved, totalQuestions, easySolved, totalEasy, mediumSolved, totalMedium, hardSolved, totalHard, acceptanceRate, ranking, contributionPoints, reputation, submissionCalendar);
    }

    private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
