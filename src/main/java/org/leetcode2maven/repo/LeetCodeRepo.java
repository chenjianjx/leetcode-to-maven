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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LeetCodeRepo {

    private static final Pattern CONTENT_EXAMPLE_OUTPUT_PATTERN = Pattern.compile("\\<strong\\>Output\\:\\<\\/strong\\>([^<]+)\\<");

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

        String query = "query questionData($titleSlug: String!) { question(titleSlug: $titleSlug) {questionId titleSlug content codeSnippets{langSlug code} exampleTestcaseList} }";

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

        //Note: the graphql api only contains "input" of the test cases
        List<TestCase> defaultTestCases = questionNode.getExampleTestcaseList().stream().map(raw -> {
            String[] pieces = StringUtils.split(raw, '\n');
            TestCase model = new TestCase();
            model.setInput(new ArrayList<>());
            for (int i = 0; i < pieces.length; i++) {
                model.getInput().add(StringUtils.trimToNull(pieces[i]));
            }
            model.setExpected(pieces[pieces.length - 1]);
            return model;
        }).collect(Collectors.toList());

        //"expected" of the test cases have to be parsed from the question content
        fillExpectedOfTestCase(defaultTestCases, questionNode.getContent());

        question.setDefaultTestCases(defaultTestCases);

        return question;
    }

    private static void fillExpectedOfTestCase(List<TestCase> defaultTestCases, String content) {
        Matcher matcher = CONTENT_EXAMPLE_OUTPUT_PATTERN.matcher(content);
        List<String> expectedList = new ArrayList<>();
        while (matcher.find()) {
            expectedList.add(matcher.group(1));
        }
        for (int i = 0; i < defaultTestCases.size(); i++) {
            if (i < expectedList.size()) {
                defaultTestCases.get(i).setExpected(StringUtils.trimToNull(expectedList.get(i)));
            }else{
                throw new IllegalStateException("Failed to find the expected output for example " + (i + i));
            }
        }
    }

}
