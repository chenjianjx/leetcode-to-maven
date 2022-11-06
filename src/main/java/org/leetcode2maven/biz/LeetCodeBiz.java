package org.leetcode2maven.biz;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.leetcode2maven.biz.dto.leetcode.LeetCodeSnippetParseResult;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.biz.support.leetcode.antlr.LeetCodeSnippetVisitor;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.model.TestCase;
import org.leetode2maven.biz.support.antlr.Java8Lexer;
import org.leetode2maven.biz.support.antlr.Java8Parser;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LeetCodeBiz {
    public static final String PUBLIC_CLASS_DECLARE_LINE_REGEX = "(public\\s+class\\s+([^\\s]+))";
    private static final Pattern PUBLIC_CLASS_DECLARE_LINE_PATTERN = Pattern.compile(PUBLIC_CLASS_DECLARE_LINE_REGEX);

    private final FreemarkerTemplateFactory freemarkerFactory;

    public LeetCodeBiz(FreemarkerTemplateFactory freemarkerFactory) {
        this.freemarkerFactory = freemarkerFactory;
    }

    public LeetCodeSnippetParseResult parseCodeSnippet(String codeSnippet) {

        Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(codeSnippet));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.classDeclaration();


        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        LeetCodeSnippetVisitor visitor = new LeetCodeSnippetVisitor(rewriter);
        visitor.visit(tree);


        SingleClassCode supportingClass = null;
        if (visitor.getSupportingClassSourceWithComment() != null) {
            supportingClass = extractSupportingClass(visitor.getSupportingClassSourceWithComment());
        }


        LeetCodeSnippetParseResult result = new LeetCodeSnippetParseResult();
        result.setClassName(visitor.getClassName());
        result.setMethodName(visitor.getMethodName());
        result.setSource(rewriter.getText());
        if (supportingClass != null) {
            result.setSupportingClassSource(supportingClass.getSource());
            result.setSupportingClassName(supportingClass.getClassName());
        }

        return result;

    }


    private SingleClassCode extractSupportingClass(String sourceWithComment) {
        String source = sourceWithComment;
        source = StringUtils.replace(source, "/**", "");
        source = StringUtils.replace(source, "/*", "");
        source = StringUtils.replace(source, "*/", "");
        source = source.replaceAll("(?m)^\\s*\\*", "");

        Matcher matcher = PUBLIC_CLASS_DECLARE_LINE_PATTERN.matcher(source);
        matcher.find();

        SingleClassCode result = new SingleClassCode();
        result.setClassName(matcher.group(2));
        result.setSource(source.substring(matcher.start())); //remove anything before "public class XXX"


        return result;
    }

    public SingleClassCode buildUnitTestCode(LeetCodeSnippetParseResult parseResult, Question question) {
        Template template = freemarkerFactory
                .getClasspathTemplate("maven-project-template/src/test/java/UnitTest.java.ftl");

        Map<String, Object> model = new HashMap<String, Object>();

        String testClassName = parseResult.getClassName() + "Test";
        model.put("testClassName", testClassName);
        model.put("targetClassName", parseResult.getClassName());
        model.put("targetInstanceName", StringUtils.uncapitalize(parseResult.getClassName()));
        model.put("methodName", parseResult.getMethodName());

        model.put("testCases", question.getDefaultTestCases().stream().map(this::leetCodeCaseStringToUnitTestCaseString).collect(Collectors.toList()));

        String source;
        try {
            Writer result = new StringWriter();
            template.process(model, result);
            source = result.toString();
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException("Failed to build the unit test code", e);
        }

        SingleClassCode result = new SingleClassCode();
        result.setSource(source);
        result.setClassName(testClassName);

        return result;
    }

    private Map<String, Object> leetCodeCaseStringToUnitTestCaseString(TestCase testCase) {
        Map<String, Object> result = new HashMap<>();
        result.put("params", testCase.getInput());
        result.put("expected", testCase.getExpected());
        return result;
    }
}


