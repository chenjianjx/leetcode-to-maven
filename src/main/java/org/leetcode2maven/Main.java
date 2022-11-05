package org.leetcode2maven;

import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import org.leetcode2maven.graphqlclient.QuestionInput;
import org.leetcode2maven.graphqlclient.QuestionNode;
import org.leetcode2maven.graphqlclient.util.QueryExecutor;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {
        QueryExecutor queryExecutor = new QueryExecutor("https://leetcode.com/graphql");


        QuestionNode questionNode = queryExecutor.questionData("{questionId title titleSlug}",
                QuestionInput.builder()
                        .withTitleSlug("tow-sum")
                        .build());
        System.err.println(questionNode);


    }
}