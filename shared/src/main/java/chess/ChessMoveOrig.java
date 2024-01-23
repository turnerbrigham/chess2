package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMoveOrig {

    public ChessMoveOrig(ChessPositionOrig startPosition, ChessPositionOrig endPosition,
                         ChessPieceOrig.PieceType promotionPiece) {
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPositionOrig getStartPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPositionOrig getEndPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPieceOrig.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }
}
