package passoffTests;

import chess.*; //
//import chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used for testing your code
 */
public class TestFactory {

    // Chess Functions
    // ------------------------------------------------------------------------------------------------------------------
    public static ChessBoardOrig getNewBoard() {
        //return new ChessBoard();
        return new ChessBoard();
    }

    public static ChessGameOrig getNewGame() {
        //return new ChessGame();
        return new ChessGame();
    }

    public static ChessPieceOrig getNewPiece(ChessGameOrig.TeamColor pieceColor, ChessPieceOrig.PieceType type) {
        //return new ChessPiece(pieceColor, type);
        return new ChessPiece(pieceColor, type);
    }

    public static ChessPositionOrig getNewPosition(int row, int col) {
        //return new ChessPosition(row, col);
        return new ChessPosition(row, col);
    }

    public static ChessMoveOrig getNewMove(ChessPositionOrig startPosition, ChessPositionOrig endPosition,
                                           ChessPieceOrig.PieceType promotionPiece) {
        //return new ChessMove(startPosition, endPosition, promotionPiece);
        return new ChessMove(startPosition, endPosition, promotionPiece);
    }
    // ------------------------------------------------------------------------------------------------------------------

    // Websocket Tests
    // ------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime() {
        /*
         * Changing this will change how long tests will wait for the server to send
         * messages.
         * 3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to
         * change as you see fit,
         * just know increasing it can make tests take longer to run.
         * (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    // ------------------------------------------------------------------------------------------------------------------

    static public ChessPositionOrig startPosition(int row, int col) {

        return getNewPosition(row, col);
    }

    static public int[][] endPositions(int[][] endPos) {
        return endPos;
    }

    static public void validateMoves(String boardText, ChessPositionOrig startPosition, int[][] endPositions) {
        var board = loadBoard(boardText);
        var testPiece = board.getPiece(startPosition);
        var validMoves = loadMoves(startPosition, endPositions);
        validateMoves(board, testPiece, startPosition, validMoves);
    }

    static public void validateMoves(ChessBoardOrig board, ChessPieceOrig testPiece, ChessPositionOrig startPosition, Set<ChessMoveOrig> validMoves) {
        var pieceMoves = new HashSet<>(testPiece.pieceMoves(board, startPosition));
        Assertions.assertEquals(validMoves, pieceMoves, "Wrong moves");
    }

    final static Map<Character, ChessPieceOrig.PieceType> charToTypeMap = Map.of(
            'p', ChessPieceOrig.PieceType.PAWN,
            'n', ChessPieceOrig.PieceType.KNIGHT,
            'r', ChessPieceOrig.PieceType.ROOK,
            'q', ChessPieceOrig.PieceType.QUEEN,
            'k', ChessPieceOrig.PieceType.KING,
            'b', ChessPieceOrig.PieceType.BISHOP);

    public static ChessBoardOrig loadBoard(String boardText) {
        var board = getNewBoard();
        int row = 8;
        int column = 1;
        for (var c : boardText.toCharArray()) {
            switch (c) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> column++;
                case '|' -> {
                }
                default -> {
                    ChessGameOrig.TeamColor color = Character.isLowerCase(c) ? ChessGameOrig.TeamColor.BLACK
                            : ChessGameOrig.TeamColor.WHITE;
                    var type = charToTypeMap.get(Character.toLowerCase(c));
                    var position = TestFactory.getNewPosition(row, column);
                    var piece = TestFactory.getNewPiece(color, type);
                    board.addPiece(position, piece);
                    column++;
                }
            }
        }
        return board;
    }

    public static Set<ChessMoveOrig> loadMoves(ChessPositionOrig startPosition, int[][] endPositions) {
        var validMoves = new HashSet<ChessMoveOrig>();
        for (var endPosition : endPositions) {
            validMoves.add(TestFactory.getNewMove(startPosition,
                    TestFactory.getNewPosition(endPosition[0], endPosition[1]), null));
        }
        return validMoves;
    }
}
