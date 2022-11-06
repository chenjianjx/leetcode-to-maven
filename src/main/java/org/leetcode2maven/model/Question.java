package org.leetcode2maven.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Question {
    private int questionId;
    private String titleSlug;
    private String javaCode;
    private List<TestCase> defaultTestCases;
}
