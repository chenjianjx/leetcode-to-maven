package org.leetcode2maven.biz;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.model.Question;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

class FileBizTest {

    FileBiz biz = new FileBiz(new FreemarkerTemplateFactory());

    @Test
    void buildPomFile() throws IOException {
        Question q1 = new Question();
        q1.setTitleSlug("two-sum");
        q1.setFrontendId(1);

        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-generated-pom.txt"), DEFAULT_CHARSET),
                biz.buildPomFile(q1)
        );
    }
}