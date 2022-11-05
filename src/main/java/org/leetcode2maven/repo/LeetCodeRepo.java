package org.leetcode2maven.repo;

import org.leetcode2maven.model.Question;
import org.leetcode2maven.repo.dataobject.RestfulProblemsResponse;
import org.leetcode2maven.repo.support.LeetCodeHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LeetCodeRepo {

    private final LeetCodeHttpClient leetCodeHttpClient;

    public LeetCodeRepo(LeetCodeHttpClient leetCodeHttpClient) {
        this.leetCodeHttpClient = leetCodeHttpClient;
    }

    private Map<Integer, String> idAndTitleSlugMap = new HashMap<>();

    //TODO: periodically refresh
    private void refreshTitleSlugAndIdMap() {
        RestfulProblemsResponse rawResponse = leetCodeHttpClient.doGet("/api/problems/all/", RestfulProblemsResponse.class);

        Map<Integer, String> newMap = rawResponse.getStat_status_pairs().stream().collect(Collectors.toMap(
                pair -> pair.getStat().getQuestion_id(),
                pair -> pair.getStat().getQuestion__title_slug()
        ));

        synchronized (this) {
            idAndTitleSlugMap.clear();
            idAndTitleSlugMap.putAll(newMap);
        }
    }

    public Question getQuestionById(int questionId) {
        if (idAndTitleSlugMap.isEmpty()) {
            refreshTitleSlugAndIdMap();
        }
        String titleSlug = this.idAndTitleSlugMap.get(questionId);
        if (titleSlug == null) {
            throw new IllegalArgumentException("Question id=" + questionId + " is not found");
        }
        return this.getQuestionByTitleSlug(titleSlug);
    }

    private Question getQuestionByTitleSlug(String titleSlug) {

        String query = "query questionData($titleSlug: String!) { question(titleSlug: $titleSlug) {questionId titleSlug  __typename  }}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("titleSlug", titleSlug);

        return leetCodeHttpClient.graphQLQuery(query, variables, Question.class);

    }

}
