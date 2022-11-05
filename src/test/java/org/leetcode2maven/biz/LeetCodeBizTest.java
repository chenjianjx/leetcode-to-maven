package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeetCodeBizTest {

    private LeetCodeBiz biz = new LeetCodeBiz();

    @Test
    void extractClassName() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-1-code-snippet.txt"), "UTF8");
        assertEquals("Solution", biz.extractClassName(javaCode));
    }
}