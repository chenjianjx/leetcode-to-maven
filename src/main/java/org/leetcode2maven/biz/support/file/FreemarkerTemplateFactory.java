package org.leetcode2maven.biz.support.file;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.IOException;

import static freemarker.template.Configuration.VERSION_2_3_31;


public class FreemarkerTemplateFactory {

    public Template getClasspathTemplate(String ftlClasspath) {
        Version freemarkerVersion = VERSION_2_3_31;
        Configuration configuration = new Configuration(freemarkerVersion);
        configuration.setLocalizedLookup(false);
        configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(freemarkerVersion).build());
        configuration.setClassForTemplateLoading(
                FreemarkerTemplateFactory.class, "/");
        try {
            return configuration.getTemplate(ftlClasspath);
        } catch (IOException e) {
            throw new IllegalStateException(ftlClasspath, e);
        }
    }

}