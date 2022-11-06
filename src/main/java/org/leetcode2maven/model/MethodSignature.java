package org.leetcode2maven.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MethodSignature {
    private String name;
    private List<String> parameterTypes = new ArrayList<>();
    private String returnType;
}
