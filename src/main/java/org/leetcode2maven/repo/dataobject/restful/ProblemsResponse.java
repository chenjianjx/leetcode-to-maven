package org.leetcode2maven.repo.dataobject.restful;

import lombok.Data;

import java.util.List;

/**
 * The result of https://leetcode.com/api/problems/{topic}/
 */
@Data
public class ProblemsResponse {

    private List<StatStatusPair> stat_status_pairs;

    @Data
    public static class StatStatusPair {
        private Stat stat;
    }

    @Data
    public static class Stat {
        private int frontend_question_id;
        private String question__title_slug;
    }

}
