package passoffTests.chessTests;

import chess.ChessGameOrig;
import chess.ChessPieceOrig;
import chess.ChessPositionOrig;
import org.junit.jupiter.api.*;

import static passoffTests.TestFactory.*;

public class ChessBoardTests {

    @Test
    @DisplayName("Add and Get Piece")
    public void getAddPiece() {
        ChessPositionOrig position = getNewPosition(4, 4);
        ChessPieceOrig piece = getNewPiece(ChessGameOrig.TeamColor.BLACK, ChessPieceOrig.PieceType.BISHOP);

        var board = getNewBoard();
        board.addPiece(position, piece);

        ChessPieceOrig foundPiece = board.getPiece(position);

        Assertions.assertEquals(piece.getPieceType(), foundPiece.getPieceType(),
                "ChessPiece returned by getPiece had the wrong piece type");
        Assertions.assertEquals(piece.getTeamColor(), foundPiece.getTeamColor(),
                "ChessPiece returned by getPiece had the wrong team color");
    }


    @Test
    @DisplayName("Reset Board")
    public void defaultGameBoard() {
        var expectedBoard = loadBoard("""
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """);

        var actualBoard = getNewBoard();
        actualBoard.resetBoard();

        Assertions.assertEquals(expectedBoard, actualBoard);
    }

}
