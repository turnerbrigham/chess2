package old_incapable_code;

public class board implements ChessBoard {
    Piece[][] thisChessBoard = new Piece[9][9];

    // Fill the array with nulls
    public board getCopy(){
        board copy = new board(); // Create a new instance of the Board class
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
    public void addPiece(ChessPosition position, ChessPiece piece) {
        thisChessBoard[position.getRow()][position.getColumn()] = new Piece();
        thisChessBoard[position.getRow()][position.getColumn()].thisTeamColor = piece.getTeamColor();
        thisChessBoard[position.getRow()][position.getColumn()].thisPieceType = piece.getPieceType();
    }
    public void deletePiece(ChessPosition position) {
        thisChessBoard[position.getRow()][position.getColumn()] = null;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return thisChessBoard[position.getRow()][position.getColumn()];
    }
    public ChessPiece getPiece2(int row, int column) {
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
                thisChessBoard[row][col] = new Piece();
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.PAWN;
            }
        }
        for (int row : new int[]{1, 8} ){
            for (int col = 1; col <= 8; col ++ ){
                thisChessBoard[row][col] = new Piece();
            }
            //setting rooks
            for (int col : new int[]{1, 8} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.ROOK;
            }
            //setting knights
            for (int col : new int[]{2, 7} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.KNIGHT;
            }
            //setting bishops
            for (int col : new int[]{3, 6} ){
                thisChessBoard[row][col].thisPieceType = ChessPiece.PieceType.BISHOP;
            }
            thisChessBoard[row][4].thisPieceType = ChessPiece.PieceType.QUEEN;
            thisChessBoard[row][5].thisPieceType = ChessPiece.PieceType.KING;
        }
        //setting team colors
        for (int row : new int[]{1,2}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGame.TeamColor.WHITE;
                }
            }
        }
        for (int row : new int[]{7,8}) {
            for (int col = 1; col <= 8; col++) {
                if (thisChessBoard[row][col] != null){
                    thisChessBoard[row][col].thisTeamColor = ChessGame.TeamColor.BLACK;
                }
            }
        }

    }
}
