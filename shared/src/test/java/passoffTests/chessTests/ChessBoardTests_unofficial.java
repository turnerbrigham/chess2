package passoffTests.chessTests;

import chess.*;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;

import static passoffTests.TestFactory.*;

public class ChessBoardTests_unofficial {
    private ChessPosition original;
    private ChessPosition equal;
    private ChessPosition different;
    @BeforeEach
    public void setUp() {
        original = passoffTests.TestFactory.getNewPosition(3, 7);
        equal = passoffTests.TestFactory.getNewPosition(3, 7);
        different = TestFactory.getNewPosition(7, 3);
    }
    @Test
    public void defaultGameBoard() {
        Assertions.assertEquals(original.hashCode(), equal.hashCode(),
                "hashCode returned different values for equal positions");
        Assertions.assertNotEquals(original.hashCode(), different.hashCode(),
                "hashCode returned the same value for different positions");

    }

}
