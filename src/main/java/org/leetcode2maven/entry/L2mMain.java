package org.leetcode2maven.entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.leetcode2maven.app.GenerationManager;
import org.leetcode2maven.app.GenerationManagerFactory;

import java.io.File;
import java.io.IOException;

public class L2mMain {

    public static void main(String[] args) throws IOException {
        int questionFrontendId = -1;

        if(args.length == 0){
            System.err.println("The command is: l2m $questionFrontendId # $questionFrontendId can be found on web, such as '876' in '876. Middle of the Linked List' ");
            return;
        }

        if (!NumberUtils.isDigits(args[0])) {
            System.err.println("questionFrontendId is not a number but " + args[0]);
            return;
        } else {
            questionFrontendId = Integer.parseInt(args[0]);
        }

        GenerationManager gm = new GenerationManagerFactory().newInstance();
        File projectDir = gm.generateMavenProject(questionFrontendId, new File("."));
        System.out.println("\n\nProject successfully generated at " + projectDir);
    }
}
