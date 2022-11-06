package org.leetcode2maven.repo;

import org.apache.commons.lang3.StringUtils;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.model.TestCase;
import org.leetcode2maven.repo.dataobject.graphql.QuestionNode;
import org.leetcode2maven.repo.dataobject.restful.ProblemsResponse;
import org.leetcode2maven.repo.support.leetcode.LeetCodeHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ProblemsResponse rawResponse = leetCodeHttpClient.doGet("/api/problems/all/", ProblemsResponse.class);

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

        String query = "query questionData($titleSlug: String!) { question(titleSlug: $titleSlug) {questionId titleSlug codeSnippets{langSlug code} exampleTestcaseList} }";

        Map<String, Object> variables = new HashMap<>();
        variables.put("titleSlug", titleSlug);

        QuestionNode questionNode = leetCodeHttpClient.graphQLQuery(query, variables, QuestionNode.class);
        return toModel(questionNode);

    }

    private Question toModel(QuestionNode questionNode) {
        Question question = new Question();
        question.setQuestionId(questionNode.getQuestionId());
        question.setTitleSlug(questionNode.getTitleSlug());
        String javaCode = questionNode.getCodeSnippets().stream().filter(cs -> cs.getLangSlug().equals("java")).findFirst().get().getCode();
        question.setJavaCode(javaCode);
        List<TestCase> defaultTestCases = questionNode.getExampleTestcaseList().stream().map(raw -> {
            String[] pieces = StringUtils.split(raw, '\n');
            TestCase model = new TestCase();
            model.setInput(new ArrayList<>());
            for (int i = 0; i < pieces.length - 1; i++) {
                model.getInput().add(StringUtils.trimToNull(pieces[i]));
            }
            model.setExpected(pieces[pieces.length - 1]);
            return model;
        }).collect(Collectors.toList());
        question.setDefaultTestCases(defaultTestCases);

        return question;
    }

}
