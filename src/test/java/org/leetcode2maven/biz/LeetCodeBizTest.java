package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

class LeetCodeBizTest {

    private LeetCodeBiz biz = new LeetCodeBiz();

    @Test
    void parseCodeSnippet_plainQuestion() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-1-original-code-snippet.txt"), DEFAULT_CHARSET);
        SingleClassCode generated = biz.parseCodeSnippet(javaCode);
        assertEquals("Solution", generated.getClassName());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-enhanced-code.txt"), DEFAULT_CHARSET)
                , generated.getSource());
    }

    @Test
    void parseCodeSnippet_questionWithTreeNode() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-235-original-code-snippet-with-TreeNode.txt"), DEFAULT_CHARSET);
        SingleClassCode generated = biz.parseCodeSnippet(javaCode);
        assertEquals("Solution", generated.getClassName());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-235-extracted-TreeNode-code.txt"), DEFAULT_CHARSET)
                , generated.getSupportingClassSource());
    }
}