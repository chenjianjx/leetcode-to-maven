package org.leetcode2maven.app;

import org.leetcode2maven.biz.FileBiz;
import org.leetcode2maven.biz.LeetCodeBiz;
import org.leetcode2maven.biz.support.file.FreemarkerTemplateFactory;
import org.leetcode2maven.repo.FileRepo;
import org.leetcode2maven.repo.LeetCodeRepo;
import org.leetcode2maven.repo.support.leetcode.LeetCodeHttpClient;

public class GenerationManagerFactory {

    public GenerationManager newInstance() {
        LeetCodeBiz leetCodeBiz = new LeetCodeBiz(new FreemarkerTemplateFactory());
        LeetCodeRepo leetCodeRepo = new LeetCodeRepo(new LeetCodeHttpClient());
        FileRepo fileRepo = new FileRepo();
        FileBiz fileBiz = new FileBiz(new FreemarkerTemplateFactory());
        return new GenerationManager(leetCodeBiz, fileBiz, leetCodeRepo, fileRepo);
    }
}
