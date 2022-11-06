package org.leetcode2maven.biz.support.leetcode.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.leetcode2maven.model.MethodSignature;
import org.leetode2maven.biz.support.antlr.Java8BaseVisitor;
import org.leetode2maven.biz.support.antlr.Java8Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.leetcode2maven.biz.LeetCodeBiz.PUBLIC_CLASS_DECLARE_LINE_REGEX;

public class LeetCodeSnippetVisitor extends Java8BaseVisitor {
    private final TokenStreamRewriter rewriter;

    private TokenStream tokenStream;


    private String className;
    private MethodSignature method = new MethodSignature();
    private String supportingClassSourceWithComment;

    private final static Pattern SUPPORTING_CLASS_DECLARE_PATTERN = Pattern.compile(PUBLIC_CLASS_DECLARE_LINE_REGEX);

    public LeetCodeSnippetVisitor(TokenStreamRewriter rewriter) {
        this.tokenStream = rewriter.getTokenStream();
        this.rewriter = rewriter;
    }

    @Override
    public Object visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        this.className = ctx.normalClassDeclaration().Identifier().getText();
        findSupportingClassSource(ctx);
        return super.visitClassDeclaration(ctx);
    }

    private void findSupportingClassSource(Java8Parser.ClassDeclarationContext ctx) {
        int thisTokenIndex = ctx.getStart().getTokenIndex();
        for (int tokenIndex = 0; tokenIndex < thisTokenIndex; tokenIndex++) {
            Token token = tokenStream.get(tokenIndex);
            if (token.getChannel() == Token.HIDDEN_CHANNEL) {
                String text = token.getText();
                if (text != null && text.contains("/**")
                        && SUPPORTING_CLASS_DECLARE_PATTERN.matcher(text).find()) {
                    this.supportingClassSourceWithComment = text;
                }
            }
        }
    }

    @Override
    public Object visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        if (ctx.methodModifier(0).getText().equals("public")) {
            Java8Parser.MethodDeclaratorContext md = ctx.methodHeader().methodDeclarator();
            this.method.setName(md.Identifier().getText());

            Java8Parser.FormalParameterListContext fpList = md.formalParameterList();
            this.method.setParameterTypes(collectParameterTypes(fpList));

            this.method.setReturnType(ctx.methodHeader().result().getText());
        }
        return super.visitMethodDeclaration(ctx);
    }

    private List<String> collectParameterTypes(Java8Parser.FormalParameterListContext fpList) {
        List<String> resultList = new ArrayList<>();
        if (fpList.formalParameters() != null) {
            resultList.addAll(fpList.formalParameters().formalParameter().stream().map(fp -> fp.unannType().getText()).collect(Collectors.toList()));
        }
        Java8Parser.LastFormalParameterContext lfp = fpList.lastFormalParameter();
        if (lfp.unannType() != null) {
            resultList.add(lfp.unannType().getText());
        } else {
            resultList.add(lfp.formalParameter().unannType().getText());
        }
        return resultList;
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

    public String getSupportingClassSourceWithComment() {
        return supportingClassSourceWithComment;
    }

    public MethodSignature getMethod() {
        return method;
    }
}
