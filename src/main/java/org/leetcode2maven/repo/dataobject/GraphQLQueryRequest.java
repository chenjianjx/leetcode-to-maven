package org.leetcode2maven.repo.dataobject;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GraphQLQueryRequest {

    private Map<String, Object> variables = new HashMap<>();
    private String query;


}
