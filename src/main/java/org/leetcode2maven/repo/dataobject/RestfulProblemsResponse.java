package org.leetcode2maven.repo.dataobject;

import lombok.Data;

import java.util.List;

/**
 * The result of https://leetcode.com/api/problems/all/
 */
@Data
public class RestfulProblemsResponse {

    private List<StatStatusPair> stat_status_pairs;

    @Data
    public static class StatStatusPair {
        private Stat stat;
    }

    @Data
    public static class Stat {
        private int question_id;
        private String question__title_slug;
    }

}
