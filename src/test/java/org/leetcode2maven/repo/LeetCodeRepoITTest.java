package org.leetcode2maven.repo;

import org.junit.jupiter.api.Test;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.repo.support.LeetCodeHttpClient;

import static org.junit.jupiter.api.Assertions.*;

class LeetCodeRepoITTest {

    private LeetCodeRepo repo = new LeetCodeRepo(new LeetCodeHttpClient());

    @Test
    void getQuestionById() {
        Question question = repo.getQuestionById(1);
        assertEquals(1, question.getQuestionId());
        assertEquals("two-sum", question.getTitleSlug());
    }
}