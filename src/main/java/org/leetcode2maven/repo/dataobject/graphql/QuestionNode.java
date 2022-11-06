package org.leetcode2maven.repo.dataobject.graphql;

import lombok.Data;

import java.util.List;

@Data
public class QuestionNode {
    private int questionFrontendId;
    private String titleSlug;
    private String content;
    private List<CodeSnippetNode> codeSnippets;
    private List<String> exampleTestcaseList;
}
