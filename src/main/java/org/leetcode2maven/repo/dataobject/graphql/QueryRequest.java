package org.leetcode2maven.repo.dataobject.graphql;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueryRequest {
    private Map<String, Object> variables;
    private String query;
}
