package org.leetcode2maven.biz.dto.leetcode;

import lombok.Data;
import org.leetcode2maven.model.MethodSignature;

@Data
public class LeetCodeSnippetParseResult {
    private String className;
    private String source;
    private MethodSignature method;
    private String supportingClassName;
    private String supportingClassSource;

    public boolean hasSupportingClass() {
        return supportingClassName != null && supportingClassSource != null;
    }
}
