import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ${testClassName} {

    ${targetClassName} ${targetInstanceName} = new ${targetClassName}();

    <#list testCases as case>
    @Test
    void case${case?index + 1}() {
        assertEquals(${case.expected}, ${targetInstanceName}.${methodName}(<#list case.params as param>${param}<#sep>, </#sep></#list>));
    }


    </#list> 
}