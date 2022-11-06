package org.leetcode2maven.manualtest;

import org.leetcode2maven.app.GenerationManager;
import org.leetcode2maven.app.GenerationManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class BatchGenTestMain {

    private final GenerationManager gm = new GenerationManagerFactory().newInstance();

    private void batchGen(List<Integer> frontendIdList) throws IOException {
        File projectParentDir = Files.createTempDirectory("leetcode2maven-BatchGenTestMain-").toFile();
        System.out.println("All projects will be under " + projectParentDir);

        for (Integer frontendId : frontendIdList) {
            try {
                gm.generateMavenProject(frontendId, projectParentDir);
            } catch (Exception e) {
                System.err.println("Failed to generate for question " + frontendId);
                e.printStackTrace();
            }
        }

        System.out.println("Done");
        System.out.println("cd " + projectParentDir);
    }

    public static void main(String[] args) throws IOException {
        new BatchGenTestMain().batchGen(
                Arrays.asList(1, 235, 542, 876)
        );
    }
}
