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
        System.out.println("Project parent dir is " + projectParentDir);
    }

    @Test
    public void generateMavenProject_plainQuestion() throws IOException {




        generationManager.generateMavenProject(1, projectParentDir);

        File projectDir = new File(projectParentDir, "lc-1-two-sum");

        File pomFile = new File(projectDir, "pom.xml");
        assertTrue(pomFile.exists());

        File solutionFile = new File(projectDir, "src/main/java/Solution.java");
        assertTrue(solutionFile.exists());

        File notesFile = new File(projectDir, "notes.md");
        assertTrue(notesFile.exists());

    }

//    @Test
//    public void generateMavenProject_questionWithSupportingClass() throws IOException {
//
//
//        generationManager.generateMavenProject(1, projectParentDir);
//
//        File projectDir = new File(projectParentDir, "lc-1-two-sum");
//
//        File pomFile = new File(projectDir, "pom.xml");
//        assertTrue(pomFile.exists());
//
//        File solutionFile = new File(projectDir, "src/main/java/Solution.java");
//        assertTrue(solutionFile.exists());
//
//        File notesFile = new File(projectDir, "notes.md");
//        assertTrue(notesFile.exists());
//
//    }
}