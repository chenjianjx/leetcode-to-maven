package org.leetcode2maven.biz;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.model.Question;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FileBiz {

    private final FreemarkerTemplateFactory freemarkerFactory;

    public FileBiz(FreemarkerTemplateFactory freemarkerFactory) {
        this.freemarkerFactory = freemarkerFactory;
    }

    public String buildPomFile(Question question) {
        Template template = freemarkerFactory
                .getClasspathTemplate("maven-project-template/pom.xml.ftl");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("questionId", question.getQuestionId());
        model.put("titleSlug", question.getTitleSlug());

        try {
            Writer result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException("Failed to create the pom.xml file", e);
        }
    }

}
