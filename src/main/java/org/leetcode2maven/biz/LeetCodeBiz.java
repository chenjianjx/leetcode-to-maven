package org.leetcode2maven.biz;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.leetcode2maven.biz.dto.leetcode.LeetCodeSnippetParseResult;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.biz.support.leetcode.antlr.LeetCodeSnippetVisitor;
import org.leetode2maven.biz.support.antlr.Java8Lexer;
import org.leetode2maven.biz.support.antlr.Java8Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeetCodeBiz {
    public static final String PUBLIC_CLASS_DECLARE_LINE_REGEX = "(public\\s+class\\s+([^\\s]+))";
    private static final Pattern PUBLIC_CLASS_DECLARE_LINE_PATTERN = Pattern.compile(PUBLIC_CLASS_DECLARE_LINE_REGEX);

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

}
