package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.dto.leetcode.LeetCodeSnippetParseResult;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.model.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

class LeetCodeBizTest {

    private LeetCodeBiz biz = new LeetCodeBiz(new FreemarkerTemplateFactory());

    @Test
    void parseCodeSnippet_plainQuestion() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-1-original-code-snippet.txt"), DEFAULT_CHARSET);
        LeetCodeSnippetParseResult generated = biz.parseCodeSnippet(javaCode);
        assertEquals("Solution", generated.getClassName());
        assertEquals("twoSum", generated.getMethodName());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-enhanced-code.txt"), DEFAULT_CHARSET)
                , generated.getSource());
    }

    @Test
    void parseCodeSnippet_questionWithTreeNode() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-235-original-code-snippet-with-TreeNode.txt"), DEFAULT_CHARSET);
        LeetCodeSnippetParseResult generated = biz.parseCodeSnippet(javaCode);
        assertEquals("TreeNode", generated.getSupportingClassName());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-235-extracted-TreeNode-code.txt"), DEFAULT_CHARSET)
                , generated.getSupportingClassSource());
    }

    @Test
    void buildUnitTestCode_plainQuestion() throws IOException {
        LeetCodeSnippetParseResult parseResult = new LeetCodeSnippetParseResult();
        parseResult.setClassName("Solution");
        parseResult.setMethodName("twoSum");
        parseResult.setSource(IOUtils.toString(this.getClass().getResource("/leetcode-1-enhanced-code.txt"), DEFAULT_CHARSET));


        Question question = new Question();
        question.setDefaultTestCases(
                Arrays.asList(
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[2,7,11,15]"))).expected("9").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,2,4]"))).expected("6").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,3]"))).expected("6").build()
                )
        );
        SingleClassCode result = biz.buildUnitTestCode(parseResult, question);

        assertEquals("SolutionTest", result.getClassName());
        assertEquals("wa ha ha", result.getSource());
    }

}