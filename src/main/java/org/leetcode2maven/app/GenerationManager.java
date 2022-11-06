package org.leetcode2maven.app;

import org.leetcode2maven.biz.FileBiz;
import org.leetcode2maven.biz.LeetCodeBiz;
import org.leetcode2maven.biz.dto.leetcode.SingleClassCode;
import org.leetcode2maven.model.Question;
import org.leetcode2maven.repo.FileRepo;
import org.leetcode2maven.repo.LeetCodeRepo;

import java.io.File;
import java.io.IOException;

public class GenerationManager {

    private final LeetCodeBiz leetCodeBiz;

    private final FileBiz fileBiz;
    private final LeetCodeRepo leetCodeRepo;

    private final FileRepo fileRepo;

    public GenerationManager(LeetCodeBiz leetCodeBiz, FileBiz fileBiz, LeetCodeRepo leetCodeRepo, FileRepo fileRepo) {
        this.leetCodeBiz = leetCodeBiz;
        this.fileBiz = fileBiz;
        this.leetCodeRepo = leetCodeRepo;
        this.fileRepo = fileRepo;
    }

    public void generateMavenProject(int questionId, File projectParentDir) throws IOException {
        validate(questionId, projectParentDir);


        Question question = leetCodeRepo.getQuestionById(questionId);

        String projectDirName = fileBiz.getProjectDirName(question);
        File projectDir = new File(projectParentDir, projectDirName);
        fileRepo.createProjectDir(projectDir);

        String pomFileContent = fileBiz.buildPomFile(question);
        fileRepo.savePomFile(projectDir, pomFileContent);


        String codeSnippet = question.getJavaCode();
        SingleClassCode code = leetCodeBiz.parseCodeSnippet(codeSnippet);
        fileRepo.saveSolutionClass(projectDir, code.getClassName(), code.getSource());

        fileRepo.saveNotesFile(projectDir);

    }

    private static void validate(int questionId, File projectDir) {
        if (questionId <= 0) {
            throw new IllegalArgumentException("Please input a valid leet code questionId");
        }

        if (projectDir == null) {
            throw new IllegalArgumentException("Please input projectDir");
        }
    }
}
