package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

class LeetCodeBizTest {

    private LeetCodeBiz biz = new LeetCodeBiz();

    @Test
    void extractClassName() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-1-code-snippet.txt"), DEFAULT_CHARSET);
        assertEquals("Solution", biz.extractClassName(javaCode));
    }
}