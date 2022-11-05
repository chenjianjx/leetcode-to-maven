package org.leetcode2maven.app;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leetcode2maven.biz.LeetCodeBiz;
import org.leetcode2maven.repo.FileRepo;
import org.leetcode2maven.repo.LeetCodeRepo;
import org.leetcode2maven.repo.support.leetcode.LeetCodeHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerationManagerITTest {

    private GenerationManager generationManager;

    @BeforeEach
    public void setUp() {

        LeetCodeBiz leetCodeBiz = new LeetCodeBiz();
        LeetCodeRepo leetCodeRepo = new LeetCodeRepo(new LeetCodeHttpClient());
        FileRepo fileRepo = new FileRepo();
        generationManager = new GenerationManager(leetCodeBiz, leetCodeRepo, fileRepo);
    }

    @Test
    public void generateMavenProject() throws IOException {
        File dir = Files.createTempDirectory("leetcode2maven-GenerationManagerITTest-").toFile();
        System.out.println("Project dir is " + dir);


        generationManager.generateMavenProject(1, dir);

        File solutionFile = new File(dir, "src/main/java/Solution.java");
        assertTrue(solutionFile.exists());
        assertEquals(
                IOUtils.toString(this.getClass().getResource("/leetcode-1-code-snippet.txt"), "UTF8"),
                FileUtils.readFileToString(solutionFile, "UTF8")
        );


    }
}