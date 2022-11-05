package org.leetcode2maven.repo;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

public class FileRepo {
    public void createProjectDir(File dir) {
        dir.mkdirs();
    }

    public void saveSolutionClass(File projectDir, String className, String javaCode) throws IOException {
        File codeDir = new File(projectDir, "src/main/java");
        codeDir.mkdirs();

        File javaFile = new File(codeDir, className + ".java");
        FileUtils.write(javaFile, javaCode, DEFAULT_CHARSET);
    }

    public void savePomFile(File projectDir, String content) throws IOException {
        File pomFile = new File(projectDir, "pom.xml");
        FileUtils.write(pomFile, content, DEFAULT_CHARSET);
    }
}
