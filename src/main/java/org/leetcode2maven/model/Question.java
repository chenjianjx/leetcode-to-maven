package org.leetcode2maven.model;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private int frontendId;
    private String titleSlug;
    private String javaCode;
    private List<TestCase> defaultTestCases;
}
