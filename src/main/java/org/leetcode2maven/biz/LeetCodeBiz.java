package org.leetcode2maven.biz;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.biz.support.leetcode.antlr.LeetCodeSnippetVisitor;
import org.leetode2maven.biz.support.antlr.Java8Lexer;
import org.leetode2maven.biz.support.antlr.Java8Parser;

public class LeetCodeBiz {
    public SingleClassCode parseCodeSnippet(String codeSnippet) {

        Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(codeSnippet));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.classDeclaration();


        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        LeetCodeSnippetVisitor visitor = new LeetCodeSnippetVisitor(rewriter);
        visitor.visit(tree);

        SingleClassCode result = new SingleClassCode();
        result.setClassName(visitor.getClassName());
        result.setSource(rewriter.getText());

        return result;

    }


}
