import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

    Solution solution = new Solution();

    @Test
    void case1() {
        assertEquals(toIntArray("[0,1]"), solution.twoSum(toIntArray("[2,7,11,15]"), 9));
    }


    @Test
    void case2() {
        assertEquals(toIntArray("[1,2]"), solution.twoSum(toIntArray("[3,2,4]"), 6));
    }


    @Test
    void case3() {
        assertEquals(toIntArray("[0,1]"), solution.twoSum(toIntArray("[3,3]"), 6));
    }




    private String toIntArray(String rawInput) {
        rawInput = rawInput.trim();
        rawInput = StringUtils.stripStart(rawInput, "[");
        rawInput = StringUtils.stripEnd(rawInput, "]");

        List<String> elements = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(rawInput, " ,");
        while (st.hasMoreTokens()) {
            elements.add(st.nextToken());
        }

        return String.format("new int[]{%s}", StringUtils.join(elements, ", "));
    }
}