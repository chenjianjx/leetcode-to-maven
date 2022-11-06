package org.leetcode2maven.biz;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.leetcode2maven.biz.support.antlr.TempJava8Visitor;
import org.leetode2maven.biz.support.antlr.Java8Lexer;
import org.leetode2maven.biz.support.antlr.Java8Parser;

public class LeetCodeBiz {
    public String extractClassName(String javaCode) {
        Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(javaCode));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.classDeclaration();

        TempJava8Visitor visitor = new TempJava8Visitor();
        visitor.visit(tree);

        return visitor.getClassName();

    }


}
