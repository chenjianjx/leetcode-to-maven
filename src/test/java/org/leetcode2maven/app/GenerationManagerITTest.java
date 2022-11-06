package org.leetcode2maven.app;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.FileBiz;
import org.leetcode2maven.biz.LeetCodeBiz;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.repo.FileRepo;
import org.leetcode2maven.repo.LeetCodeRepo;
import org.leetcode2maven.repo.support.leetcode.LeetCodeHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.leetcode2maven.global.GlobalConstants.DEFAULT_CHARSET;

class GenerationManagerITTest {

    private GenerationManager generationManager;

    @BeforeEach
    public void setUp() {

        LeetCodeBiz leetCodeBiz = new LeetCodeBiz();
        LeetCodeRepo leetCodeRepo = new LeetCodeRepo(new LeetCodeHttpClient());
        FileRepo fileRepo = new FileRepo();
        FileBiz fileBiz = new FileBiz(new FreemarkerTemplateFactory());
        generationManager = new GenerationManager(leetCodeBiz, fileBiz, leetCodeRepo, fileRepo);
    }

    @Test
    public void generateMavenProject() throws IOException {
        File parentDir = Files.createTempDirectory("leetcode2maven-GenerationManagerITTest-").toFile();
        System.out.println("Project parent dir is " + parentDir);


        generationManager.generateMavenProject(1, parentDir);

        File projectDir = new File(parentDir, "lc-1-two-sum");

        File pomFile = new File(projectDir, "pom.xml");
        assertTrue(pomFile.exists());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-generated-pom.txt"), DEFAULT_CHARSET),
                FileUtils.readFileToString(pomFile, DEFAULT_CHARSET)
        );

        File solutionFile = new File(projectDir, "src/main/java/Solution.java");
        assertTrue(solutionFile.exists());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-enhanced-code.txt"), DEFAULT_CHARSET),
                FileUtils.readFileToString(solutionFile, DEFAULT_CHARSET)
        );


        File notesFile = new File(projectDir, "notes.md");
        assertTrue(notesFile.exists());

    }
}