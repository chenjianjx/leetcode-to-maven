package org.leetcode2maven.biz.dto.leetcode;

import lombok.Data;

@Data
public class LeetCodeSnippetParseResult {
    private String className;
    private String source;
    private String methodName;
    private String supportingClassName;
    private String supportingClassSource;

    public boolean hasSupportingClass() {
        return supportingClassName != null && supportingClassSource != null;
    }
}
