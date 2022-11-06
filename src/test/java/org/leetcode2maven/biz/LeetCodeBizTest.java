package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.dto.leetcode.LeetCodeSnippetParseResult;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.model.MethodSignature;
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
        assertEquals("twoSum", generated.getMethod().getName());
        assertEquals(Arrays.asList("int[]", "int"), generated.getMethod().getParameterTypes());
        assertEquals("int[]", generated.getMethod().getReturnType());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-enhanced-code.txt"), DEFAULT_CHARSET)
                , generated.getSource());
    }

    @Test
    void parseCodeSnippet_questionWithTreeNode() throws IOException {
        String javaCode = IOUtils.toString(this.getClass().getResource("/leetcode-235-original-code-snippet-with-TreeNode.txt"), DEFAULT_CHARSET);
        LeetCodeSnippetParseResult parseResult = biz.parseCodeSnippet(javaCode);
        assertEquals("TreeNode", parseResult.getSupportingClassName());
        assertEquals(Arrays.asList("TreeNode", "TreeNode", "TreeNode"), parseResult.getMethod().getParameterTypes());
        assertEquals("TreeNode", parseResult.getMethod().getReturnType());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-235-enhanced-code.txt"), DEFAULT_CHARSET)
                , parseResult.getSource());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-235-extracted-TreeNode-code.txt"), DEFAULT_CHARSET).trim()
                , parseResult.getSupportingClassSource().trim());
    }

    @Test
    void buildUnitTestCode_plainQuestion() throws IOException {
        LeetCodeSnippetParseResult parseResult = new LeetCodeSnippetParseResult();
        parseResult.setClassName("Solution");
        parseResult.setMethod(new MethodSignature());
        parseResult.getMethod().setName("twoSum");
        parseResult.getMethod().setParameterTypes(Arrays.asList("int[]", "int"));
        parseResult.getMethod().setReturnType("int[]");


        Question question = new Question();
        question.setDefaultTestCases(
                Arrays.asList(
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[2,7,11,15]", "9"))).expected("[0,1]").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,2,4]", "6"))).expected("[1,2]").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[3,3]", "6"))).expected("[0,1]").build()
                )
        );
        SingleClassCode result = biz.buildUnitTestCode(parseResult, question);


        assertEquals("SolutionTest", result.getClassName());
        assertEquals(IOUtils.toString(this.getClass().getResource("/leetcode-1-generated-test.txt"), DEFAULT_CHARSET), result.getSource());
    }


    @Test
    void buildUnitTestCode_questionWithTreeNode() throws IOException {
        LeetCodeSnippetParseResult parseResult = new LeetCodeSnippetParseResult();
        parseResult.setClassName("Solution");
        parseResult.setMethod(new MethodSignature());
        parseResult.getMethod().setName("lowestCommonAncestor");
        parseResult.getMethod().setParameterTypes(Arrays.asList("TreeNode", "TreeNode", "TreeNode"));
        parseResult.getMethod().setReturnType("TreeNode");


        Question question = new Question();
        question.setDefaultTestCases(
                Arrays.asList(
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[6,2,8,0,4,7,9,null,null,3,5]", "2" ,"8"))).expected("6").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[6,2,8,0,4,7,9,null,null,3,5]", "2", "4"))).expected("2").build(),
                        TestCase.builder().input(new ArrayList<>(Arrays.asList("[2,1]", "2", "1"))).expected("2").build()
                )
        );
        SingleClassCode result = biz.buildUnitTestCode(parseResult, question);


        assertEquals("SolutionTest", result.getClassName());
        assertEquals(IOUtils.toString(this.getClass().getResource("/leetcode-235-generated-test.txt"), DEFAULT_CHARSET), result.getSource());
    }
}