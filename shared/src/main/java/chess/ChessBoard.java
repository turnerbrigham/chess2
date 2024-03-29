package chess;

import java.util.Arrays;

public class ChessBoard extends ChessBoardOrig {
    ChessPiece[][] thisChessBoard = new ChessPiece[9][9];

    // Fill the array with nulls
    public ChessBoard getCopy(){
        ChessBoard copy = new ChessBoard(); // Create a new instance of the Board class
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ( thisChessBoard[i][j]  == null ){
                    copy.thisChessBoard[i][j] = null;
                }
                else{
                    copy.thisChessBoard[i][j] = thisChessBoard[i][j].getCopy();
                }
            }
        }
        return copy;
    }
    @Override
    public void addPiece(ChessPositionOrig position, ChessPieceOrig piece) {
//        thisChessBoard[position.getRow()][position.getColumn()] = new Piece();
//        thisChessBoard[position.getRow()][position.getColumn()].thisTeamColor = piece.getTeamColor();
//        thisChessBoard[position.getRow()][position.getColumn()].thisPieceType = piece.getPieceType();
        //public Piece(ChessGame.TeamColor pieceColor, PieceType type) {
        thisChessBoard[position.getRow()][position.getColumn()] = new ChessPiece(piece.getTeamColor() , piece.getPieceType() );

    }
    public void deletePiece(ChessPositionOrig position) {
        thisChessBoard[position.getRow()][position.getColumn()] = null;
    }

    @Override
    public ChessPieceOrig getPiece(ChessPositionOrig position) {
        return thisChessBoard[position.getRow()][position.getColumn()];
    }
    public ChessPieceOrig getPiece2(int row, int column) {
        return thisChessBoard[row][column];
    }
    @Override
    public void resetBoard() {


        //initiating to null
        for (int row = 0; row <= 8; row++) {
            for (int col = 0; col <= 8; col++) {
                thisChessBoard[row][col] = null;
            }
        }
        //setting pawns
        for (int row : new int[]{2, 7} ){
            for (int col = 1; col <= 8; col++ ){
                //thisChessBoard[row][col] = new Piece();
                //thisChessBoard[row][col].thisPieceType = chess.ChessPiece.PieceType.PAWN;

                //public Piece(ChessGame.TeamColor pieceColor, PieceType type) {
                thisChessBoard[row][col] = new ChessPiece( null , ChessPieceOrig.PieceType.PAWN );
            }
        }
        for (int row : new int[]{1, 8} ){
            for (int col = 1; col <= 8; col ++ ){
                thisChessBoard[row][col] = new ChessPiece( null , null );
            }
            //setting rooks
            for (int col : new int[]{1, 8} ){
                thisChessBoard[row][col].thisPieceType = ChessPieceOrig.PieceType.ROOK;
            }
            //setting knights
            for (int col : new int[]{2, 7} ){
                thisChessBoard[row][col].thisPieceType = ChessPieceOrig.PieceType.KNIGHT;
            }
            //setting bishops
            for (int col : new int[]{3, 6} ){
                thisChessBoard[row][col].thisPieceType = ChessPieceOrig.PieceType.BISHOP;
            }
            thisChessBoard[row][4].thisPieceType = ChessPieceOrig.PieceType.QUEEN;
            thisChessBoard[row][5].thisPieceType = ChessPieceOrig.PieceType.KING;
        }
        //setting team colors
        for (int row : new int[]{1,2}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGameOrig.TeamColor.WHITE;
                }
            }
        }
        for (int row : new int[]{7,8}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGameOrig.TeamColor.BLACK;
                }
            }
        }

    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) obj;
        // Check equality for the 2D array
        return Arrays.deepEquals(this.thisChessBoard , other.thisChessBoard );
    }
}
