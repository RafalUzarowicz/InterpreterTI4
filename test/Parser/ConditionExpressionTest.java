package Parser;

import Scanner.Scanner;
import Source.StringSource;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConditionExpressionTest {

    private static Stream<Arguments> provideOrCondition() {
        return Stream.of(
                Arguments.of("true||false", "!((x||(!(y)+2)))"),
                Arguments.of("2+2*2", "(2+(2*2))")
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrCondition")
    void orCondition(String line, String expected) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        OrCondition orCondition = parser.tryOrCondition();
        // Assert
        assertNotEquals(null, orCondition);
        assertEquals(expected, orCondition.toString());
    }
}
