package org.leetcode2maven.app;

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

class GenerationManagerITTest {

    GenerationManager generationManager;

    File projectParentDir;

    @BeforeEach
    public void setUp() throws IOException {

        LeetCodeBiz leetCodeBiz = new LeetCodeBiz();
        LeetCodeRepo leetCodeRepo = new LeetCodeRepo(new LeetCodeHttpClient());
        FileRepo fileRepo = new FileRepo();
        FileBiz fileBiz = new FileBiz(new FreemarkerTemplateFactory());
        generationManager = new GenerationManager(leetCodeBiz, fileBiz, leetCodeRepo, fileRepo);

        projectParentDir = Files.createTempDirectory("leetcode2maven-GenerationManagerITTest-").toFile();

    }

    @Test
    public void generateMavenProject_plainQuestion() throws IOException {

        File expectedProjectDir = new File(projectParentDir, "lc-1-two-sum");
        assertEquals(expectedProjectDir, generationManager.generateMavenProject(1, projectParentDir));
        System.out.println("Project dir is " + expectedProjectDir);

        assertTrue(new File(expectedProjectDir, "pom.xml").exists());
        assertTrue(new File(expectedProjectDir, "src/main/java/Solution.java").exists());
        assertTrue(new File(expectedProjectDir, "notes.md").exists());

    }

    @Test
    public void generateMavenProject_questionWithSupportingClass() throws IOException {

        File expectedProjectDir = new File(projectParentDir, "lc-235-lowest-common-ancestor-of-a-binary-search-tree");
        assertEquals(expectedProjectDir, generationManager.generateMavenProject(235, projectParentDir));
        System.out.println("Project dir is " + expectedProjectDir);

        assertTrue(new File(expectedProjectDir, "src/main/java/TreeNode.java").exists());
    }
}