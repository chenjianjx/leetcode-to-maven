package org.leetcode2maven.biz.support.antlr;

import org.leetode2maven.biz.support.antlr.Java8BaseVisitor;
import org.leetode2maven.biz.support.antlr.Java8Parser;

public class TempJava8Visitor extends Java8BaseVisitor {

    private String className;

    @Override
    public Object visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        this.className = ctx.normalClassDeclaration().Identifier().getText();
        return super.visitClassDeclaration(ctx);
    }

    public String getClassName() {
        return className;
    }
}
