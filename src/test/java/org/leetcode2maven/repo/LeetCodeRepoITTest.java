package org.leetcode2maven.repo;

import org.junit.jupiter.api.Test;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.model.TestCase;
import org.leetcode2maven.repo.support.leetcode.LeetCodeHttpClient;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LeetCodeRepoITTest {

    private LeetCodeRepo repo = new LeetCodeRepo(new LeetCodeHttpClient());

    @Test
    void getQuestionById() {
        Question question = repo.getQuestionById(1);
        assertEquals(1, question.getQuestionId());
        assertEquals("two-sum", question.getTitleSlug());
        assertEquals("class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        \n    }\n}", question.getJavaCode());
        assertArrayEquals(
                new Object[]{
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[2,7,11,15]", "9"))).expected("[0,1]").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,2,4]", "6"))).expected("[1,2]").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,3]","6"))).expected("[0,1]").build()
                },
                question.getDefaultTestCases().toArray());
    }
}