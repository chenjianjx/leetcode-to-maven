import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ${testClassName} {

    ${targetClassName} ${targetInstanceName} = new ${targetClassName}();

    <#list testCases as case>
    @Test
    void case${case?index + 1}() {
        assertEquals(${case.expected}, ${targetInstanceName}.${methodName}(<#list case.params as param>${param}<#sep>, </#sep></#list>));
    }


    </#list>

    <#if utilMethods?seq_contains("printIntArray")>
    private void printIntArray(int[] array) {
        System.out.println(Arrays.stream(array).boxed().collect(Collectors.toList()));
    }
    </#if>

    <#if utilMethods?seq_contains("printTwoDIntArray")>
    private void printTwoDIntArray(int[][] array) {
        Arrays.stream(array).forEach(row -> {
            printIntArray(row);
        });
    }
    </#if>

    <#if utilMethods?seq_contains("toIntArray")>
    private int[] toIntArray(String rawInput) {
        rawInput = rawInput.replaceAll("\\[", "");
        rawInput = rawInput.replaceAll("\\]", "");
        rawInput = rawInput.trim();


        List<Integer> elements = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(rawInput, " ,");
        while (st.hasMoreTokens()) {
            elements.add(Integer.parseInt(st.nextToken()));
        }
        return elements.stream().mapToInt(i -> i).toArray();
    }
    </#if>

    <#if utilMethods?seq_contains("toTwoDIntArray")>
    private int[][] toTwoDIntArray(String rawInput) {

        Pattern rowPattern = Pattern.compile("\\[\\s*\\d\\s*(\\s*,\\s*\\d\\s*)*\\]");
        Matcher matcher = rowPattern.matcher(rawInput);

        List<int[]> rows = new ArrayList<>();
        while (matcher.find()) {
            String row = matcher.group();
            rows.add(toIntArray(row));
        }

        return rows.stream().toArray(int[][]::new);
    }
    </#if>

    <#if utilMethods?seq_contains("toTreeNode")>
    //See https://leetcode.com/problems/recover-binary-search-tree/discuss/32539/Tree-Deserializer-and-Visualizer-for-Python/465627
    private TreeNode toTreeNode(String rawInput) {
        rawInput = rawInput.replaceAll("\\[", "");
        rawInput = rawInput.replaceAll("\\]", "");
        rawInput = rawInput.trim();

        List<Integer> nodes = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(rawInput, " ,");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nodes.add(
                    (token.equals("null")) ? null : Integer.valueOf(token)
            );
        }
        if (nodes.isEmpty()) {
            return null;
        }

        TreeNode[] madeNodes = new TreeNode[nodes.size()];
        Stack<TreeNode> stack = new Stack<>();
        int n = 0;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            TreeNode node = (nodes.get(i) == null) ? null : new TreeNode(nodes.get(i));
            madeNodes[nodes.size() - 1 - (n++)] = node;
            stack.push(node);
        }

        TreeNode root = stack.pop();
        for (TreeNode node : madeNodes) {
            if (node != null) {
                if (!stack.empty()) node.left = stack.pop();
                if (!stack.empty()) node.right = stack.pop();
            }
        }
        return root;
    }
    </#if>


    <#if utilMethods?seq_contains("toListNode")>
    //See https://leetcode.com/problems/recover-binary-search-tree/discuss/32539/Tree-Deserializer-and-Visualizer-for-Python/465627
    ListNode toListNode(String rawInput) {
        rawInput = rawInput.replaceAll("\\[", "");
        rawInput = rawInput.replaceAll("\\]", "");
        rawInput = rawInput.trim();

        List<Integer> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(rawInput, " ,");
        while (st.hasMoreTokens()) {
            list.add(Integer.parseInt(st.nextToken()));
        }

        ListNode listNode = null;
        for (int k = list.size() - 1; k >= 0; k--) {
            ListNode node = new ListNode(list.get(k));
            node.next = listNode;
            listNode = node;
        }
        return listNode;
    }
    </#if>
}