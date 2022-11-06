package org.leetcode2maven.repo;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

public class FileRepo {
    public void createProjectDir(File dir) {
        if (dir.exists() && dir.listFiles().length > 0) {
            throw new IllegalArgumentException(dir + " is not empty.");
        }
        dir.mkdirs();
    }

    public void saveSolutionClass(File projectDir, String className, String javaCode) throws IOException {
        File codeDir = new File(projectDir, "src/main/java");
        codeDir.mkdirs();

        File javaFile = new File(codeDir, className + ".java");
        FileUtils.write(javaFile, javaCode, DEFAULT_CHARSET);
    }

    public void saveSupportingClass(File projectDir, String supportingClassName, String supportingClassSource) throws IOException {
        File codeDir = new File(projectDir, "src/main/java");
        codeDir.mkdirs();

        File javaFile = new File(codeDir, supportingClassName + ".java");
        FileUtils.write(javaFile, supportingClassSource, DEFAULT_CHARSET);
    }

    public void savePomFile(File projectDir, String content) throws IOException {
        File pomFile = new File(projectDir, "pom.xml");
        FileUtils.write(pomFile, content, DEFAULT_CHARSET);
    }

    public void saveNotesFile(File projectDir) throws IOException {
        File destin = new File(projectDir, "notes.md");
        IOUtils.copy(this.getClass().getResource("/maven-project-template/notes.md"), destin);

    }


}
