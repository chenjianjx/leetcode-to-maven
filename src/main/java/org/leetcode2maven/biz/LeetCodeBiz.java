package org.leetcode2maven.biz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeetCodeBiz {
    public String extractClassName(String javaCode) {
        String regex = "class\\s+([a-zA-Z0-9_]+)\\s+\\{";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(javaCode);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("No class name found in code: " + javaCode);
        }
    }
}
