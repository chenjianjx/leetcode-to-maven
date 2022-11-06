package org.leetcode2maven.biz.support.antlr;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.leetode2maven.biz.support.antlr.Java8BaseVisitor;
import org.leetode2maven.biz.support.antlr.Java8Parser;

public class TempJava8Visitor extends Java8BaseVisitor {
    private final TokenStreamRewriter rewriter;


    private String className;

    public TempJava8Visitor(TokenStreamRewriter rewriter) {
        this.rewriter = rewriter;
    }

    @Override
    public Object visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        this.className = ctx.normalClassDeclaration().Identifier().getText();
        return super.visitClassDeclaration(ctx);
    }

    @Override
    public Object visitMethodBody(Java8Parser.MethodBodyContext ctx) {
        String text = ctx.getText();
        if (text.trim().equals("{}")) {
            //TODO: is this the right way to change code in Antlr?
            rewriter.replace(ctx.getStart().getTokenIndex(),
                    ctx.getStop().getTokenIndex(),
                    "{\n\t\tthrow new UnsupportedOperationException();\n\t}");
        }
        return super.visitMethodBody(ctx);
    }

    public String getClassName() {
        return className;
    }
}
