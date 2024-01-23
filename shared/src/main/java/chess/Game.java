package chess;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class Game extends ChessGame {
    public TeamColor myTurn = TeamColor.WHITE;
    public board myBoard = new board();
    public Position enPassantOpportunity = null;
    public boolean castleLeftAllowed_white = true; //false;
    public boolean castleRightAllowed_white = true; //false;
    public boolean castleLeftAllowed_black = true; //false;
    public boolean castleRightAllowed_black = true; //false;

    int gameID;
    private String whiteUsername;
    private String blackUsername;
    public void setGameID( int newGameID ){
        gameID = newGameID;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    public int getGameID( ){
        return gameID;
    }
    public Game getCopy(){
        Game newGame = new Game();
        newGame.myTurn = myTurn;
        newGame.myBoard = myBoard.getCopy();

        newGame.castleLeftAllowed_white = castleLeftAllowed_white;
        newGame.castleRightAllowed_white = castleRightAllowed_white;
        newGame.castleLeftAllowed_black = castleLeftAllowed_black;
        newGame.castleRightAllowed_black = castleRightAllowed_black;

        return newGame;
    }
    public void forceMove(chess.ChessMove movee) {
        // what does this ?
        Move move2 = Move.convertToHigher(movee);
        Piece pieceCopy = Piece.convertToHigher(myBoard.getPiece(move2.getStartPosition()));
        if (movee.getPromotionPiece() != null) {
            pieceCopy.thisPieceType = move2.promotionPiece;
        }

        //killing enemy because of en passant:
        if (pieceCopy.thisPieceType == chess.ChessPiece.PieceType.PAWN) {
            if (move2.getStartPosition().getColumn() != move2.getEndPosition().getColumn()) {
                if (myBoard.getPiece(move2.getEndPosition()) == null) {
                    //yeah, an en passant happened
                    myBoard.thisChessBoard[move2.getStartPosition().getRow()][
                            move2.getEndPosition().getColumn()] = null;
                }
            }
        }
        //checking for enPassants
        int rowMovement = move2.getEndPosition().getRow() - move2.getStartPosition().getRow();
        //System.out.println( "at least rows are good:" + (rowMovement == 2 || rowMovement == -2) );
        if (pieceCopy.thisPieceType == chess.ChessPiece.PieceType.PAWN &&
                (rowMovement == 2 || rowMovement == -2)) {
            enPassantOpportunity = new Position(move2.getEndPosition().getRow(), move2.getEndPosition().getColumn());
        } else {
            enPassantOpportunity = null;
        }

        myBoard.addPiece(move2.getEndPosition(), pieceCopy); //myBoard.getPiece(move2.getStartPosition()) );
        myBoard.deletePiece(move2.getStartPosition());

        int sRow = move2.getStartPosition().getRow();
        int sCol = move2.getStartPosition().getColumn();
        int eRow = move2.getEndPosition().getRow();
        int eCol = move2.getEndPosition().getColumn();

        if ( pieceCopy.thisPieceType == chess.ChessPiece.PieceType.KING ) {
            if ( pieceCopy.thisTeamColor == TeamColor.WHITE ) {
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 3 && castleLeftAllowed_white) {
                    forceMove(new Move(new Position(1, 1), new Position(1, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 7 && castleRightAllowed_white) {
                    forceMove(new Move(new Position(1, 8), new Position(1, 6) , movee.getPromotionPiece() ));
                    swapTurn();
                }
            }
            if ( pieceCopy.thisTeamColor == TeamColor.BLACK ) {
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 3 && castleLeftAllowed_black) {
                    forceMove(new Move(new Position(8, 1), new Position(8, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 7 && castleRightAllowed_black) {
                    forceMove(new Move(new Position(8, 8), new Position(8, 6) , movee.getPromotionPiece() ));
                    swapTurn();
                }
            }
        }
        /////////////////

        if ( sRow == 1 && sCol == 1 ){
            castleLeftAllowed_white = false;
        }
        if ( sRow == 1 && sCol == 8 ){
            castleRightAllowed_white = false;
        }
        if ( sRow == 8 && sCol == 1 ){
            castleLeftAllowed_black = false;
        }
        if ( sRow == 8 && sCol == 8 ){
            castleRightAllowed_black = false;
        }
        if ( sRow == 1 && sCol == 5 ){
            castleRightAllowed_white = false;
            castleLeftAllowed_white = false;
        }
        if ( sRow == 8 && sCol == 5 ){
            castleRightAllowed_black = false;
            castleLeftAllowed_black = false;
        }

        swapTurn();
    }
    boolean willThereBeACheck( chess.ChessMove movee ){
        Game stagedGame = getCopy();
        TeamColor presentColor = stagedGame.getBoard().getPiece( movee.getStartPosition() ).getTeamColor();
        stagedGame.forceMove( movee );
        return stagedGame.isInCheck( presentColor );
    }
    boolean isMeerlyClearOfBlocks( Position thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return true;
    }
    boolean isClearOfBlocksOrChecks( Position thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return ( !willThereBeACheck( new Move( new Position(thisPosition.row,5) , thisPosition ) ) );
    }
    boolean isClearOfChecks( Position thisPosition  ){
        return ( !willThereBeACheck( new Move( new Position(thisPosition.row,5) , thisPosition ) ) );
    }
    boolean sectionClearOfStuff( Position thisPosition ){
        int directionn;
        if ( thisPosition.column == 1 ){
            directionn = -1;
        }
        else{
            directionn = 1;
        }
        for ( int col = 5+directionn; col != (5+ 3*directionn); col += directionn ){
            if ( !isClearOfBlocksOrChecks( new Position( thisPosition.row , col )  ) ){
                return false;
            }
        }
//        if ( !isClearOfChecks( thisPosition  ) ){
//            return false;
//        }
        if ( !isMeerlyClearOfBlocks( new Position( thisPosition.row, thisPosition.column-directionn ) )){
            return false;
        }

        if ( !isClearOfChecks( new Position( thisPosition.row , 5 )  ) ){
            return false;
        }
        if ( thisPosition.row == 1 ){
            chess.ChessPiece rookPiece = myBoard.getPiece( new Position(1,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != chess.ChessPiece.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
            chess.ChessPiece kingPiece = myBoard.getPiece( new Position(1,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != chess.ChessPiece.PieceType.KING || kingPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
        }
        if ( thisPosition.row == 8 ){
            chess.ChessPiece rookPiece = myBoard.getPiece( new Position(8,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != chess.ChessPiece.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
            chess.ChessPiece kingPiece = myBoard.getPiece( new Position(8,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != chess.ChessPiece.PieceType.KING || kingPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
        }

        return true;
    }
    public Collection<chess.ChessMove> pinOnCastling(chess.ChessPosition startPosition ){
        Set<chess.ChessMove> emoves;
        emoves = new HashSet<>();
        chess.ChessPiece potentialPiece = myBoard.getPiece( startPosition );
        if (potentialPiece == null){
            return  emoves;
        }
        Collection<chess.ChessMove> movesFinal = potentialPiece.pieceMoves(  myBoard , startPosition );
        // yay castling!!
        if ( castleLeftAllowed_white && startPosition.getRow()==1 &&
                                        startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new Position( 1 , 1) ) ){
                movesFinal.add( new Move(  startPosition , new Position(1 , 3 ) ) );
            }
        }
        if ( castleRightAllowed_white && startPosition.getRow()==1 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new Position( 1 , 8) ) ){
                movesFinal.add( new Move(  startPosition , new Position(1 , 7 ) ) );
            }
        }
        if ( castleLeftAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){
            //System.out.println(  "      fortun status:" + sectionClearOfStuff( new position( 8 , 1) ) );
            if (sectionClearOfStuff( new Position( 8 , 1) ) ){
                //System.out.println( "   even got inside ");
                movesFinal.add( new Move(  startPosition , new Position(8 , 3 ) ) );
            }
        }
        //System.out.println( "       giving up:"+castleLeftAllowed_black);
        if ( castleRightAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new Position( 8 , 8) ) ){
                movesFinal.add( new Move(  startPosition , new Position(8 , 7 ) ) );
            }
        }
        // en passant
        if (enPassantOpportunity != null){
            //System.out.println("  got first gate");
            int colDiff = enPassantOpportunity.getColumn() - startPosition.getColumn();
            if ( startPosition.getRow() == enPassantOpportunity.getRow() &&
                                            (colDiff==1 || colDiff==-1) ){
                //System.out.println("    got second gate");
                if (myBoard.getPiece( startPosition ).getTeamColor() == TeamColor.WHITE){
                    movesFinal.add( new Move(  startPosition ,
                       new Position( startPosition.getRow()+1 , enPassantOpportunity.getColumn() ) ) );
                }
                else {
                    movesFinal.add( new Move(  startPosition ,
                       new Position(startPosition.getRow()-1 , enPassantOpportunity.getColumn()  ) ) );
                }
            }
        }
        return movesFinal;
    }
    @Override
    public Collection<chess.ChessMove> validMoves(chess.ChessPosition startPosition) {
        //do care about check, but don't care about being in turn or not
        Set<chess.ChessMove> emoves;
        emoves = new HashSet<>();
        // first checking if the position is empty
//        ChessPiece potentialPiece = myBoard.getPiece( startPosition );
//        if (potentialPiece == null){
//            return  emoves;
//        }
//        Collection<ChessMove> movesFinal = potentialPiece.p/ieceMoves(  myBoard , startPosition );
        //System.out.println(" getting valid moves");
        Collection<chess.ChessMove> movesFinal = pinOnCastling( startPosition );
        Iterator<chess.ChessMove> iterator = movesFinal.iterator();
        while (iterator.hasNext()) {
            chess.ChessMove presentMove = iterator.next();
            if (willThereBeACheck(presentMove)) {
                iterator.remove();
            }
        }
        return movesFinal;
    }
    public boolean isValidMove(Move movee){
        //don't care about check, but do care about being in turn or not
//        ChessPiece potentialPiece = myBoard.getPiece( movee.getStartPosition() );
//        if (potentialPiece == null){
//            return false;
//        }
//        Collection<ChessMove> myValidMoves = potentialPiece.p/ieceMoves(  myBoard , movee.getStartPosition() );
        //System.out.println("  specifically is valid move");
        Collection<chess.ChessMove> myValidMoves = pinOnCastling( movee.getStartPosition() );

        if ( myValidMoves == null ){
            return false;
        }
        boolean isInIt = false;
        int endPositionRow    = movee.getEndPosition().getRow();
        int endPositionColumn = movee.getEndPosition().getColumn();
        for ( chess.ChessMove possibleMove : myValidMoves ) {
            ChessPosition positionFromIter = possibleMove.getEndPosition();
            if (   positionFromIter.getRow() == endPositionRow &&
                    positionFromIter.getColumn() == endPositionColumn){
                isInIt = true;
            }
        }
        if ( !isInIt ){
            return false;
        }
        if ( myBoard.getPiece(movee.getStartPosition()).getTeamColor() != myTurn ){
            return false;
        }
        return true;
    }

    public Collection<chess.ChessMove> literallyAllPossibleMoves(TeamColor teamColor) {
        Collection<chess.ChessMove> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Position pos = new Position( i, j);
                chess.ChessPiece pieceHere = myBoard.getPiece( pos );
                if ( pieceHere!= null ){
                    if (pieceHere.getTeamColor() == teamColor){
                        //Collection<ChessMove> possibleMovesHere = pieceHere.p/ieceMoves( myBoard , pos );
                        //System.out.println("   getting all possible notes literally");
                        Collection<chess.ChessMove> possibleMovesHere = pinOnCastling( pos );

                        if (possibleMovesHere != null){
                            moves.addAll( possibleMovesHere );
                        }
                    }
                }
            }
        }
        return moves;
    }
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        System.out.print("isin checkmate");
        Collection<chess.ChessMove> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( chess.ChessMove possibleMove : allPossibleMoves ) {
            if ( !willThereBeACheck( possibleMove ) ){
                return false;
            }
        }
        return true;

    }
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if ( isInCheck(teamColor) ){
            return false;
        }
        System.out.println("in stale");
        Collection<chess.ChessMove> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( chess.ChessMove possibleMove : allPossibleMoves ) {
            if ( !willThereBeACheck( possibleMove ) ){
                return false;
            }
        }
        return true;

    }
    @Override
    public TeamColor getTeamTurn() {
        return myTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        myTurn = team;
    }
    @Override
    public void makeMove(chess.ChessMove movee) throws chess.InvalidMoveException {
        if (  !isValidMove( Move.convertToHigher(movee) )  ){
            throw new chess.InvalidMoveException();
        }
        if (  willThereBeACheck( movee ) ){
            throw new chess.InvalidMoveException();
        }
        forceMove( movee );
    }
    void swapTurn(){
        if (myTurn == TeamColor.WHITE){
            myTurn = TeamColor.BLACK;
        } else {
            myTurn = TeamColor.WHITE;
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        int kingRow = 0;
        int kingCol = 0;
        outerLoop:
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                chess.ChessPiece presentPiece = myBoard.getPiece2(i,j);
                if ( presentPiece != null ){
                    if (presentPiece.getPieceType() == chess.ChessPiece.PieceType.KING &&
                            presentPiece.getTeamColor() == teamColor  ){
                        kingRow = i;
                        kingCol = j;
                        break outerLoop;
                    }
                }
            }
        }
        TeamColor otherTeamColor;
        if (teamColor == TeamColor.BLACK){
            otherTeamColor = TeamColor.WHITE;
        } else {
            otherTeamColor = TeamColor.BLACK;
        }
        //System.out.print("checking if in check");
        Collection<chess.ChessMove> allMoves = literallyAllPossibleMoves( otherTeamColor );
        for ( ChessMove possibleMove : allMoves ) {
            if ( possibleMove.getEndPosition().getRow() == kingRow &&
                    possibleMove.getEndPosition().getColumn() == kingCol ){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setBoard(chess.ChessBoard board) {
        Piece[][] newChessBoard = new Piece[9][9];
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                chess.ChessPiece oldPiece = board.getPiece( new Position(i,j) );
                if (oldPiece != null) {
                    //System.out.println("something happened");
                    //Piece newPiece = new Piece();
                    //newPiece.thisPieceType = oldPiece.getPieceType();
                    //newPiece.thisTeamColor = oldPiece.getTeamColor();

                    //public Piece(ChessGame.TeamColor pieceColor, PieceType type) {
                    Piece newPiece = new Piece(oldPiece.getTeamColor() , oldPiece.getPieceType());

                    newChessBoard[i][j] = newPiece;
                }
            }
        }
        myBoard.thisChessBoard =  newChessBoard;

        castleLeftAllowed_white = true;
        castleRightAllowed_white = true;
        castleLeftAllowed_black = true;
        castleRightAllowed_black = true;
    }

    @Override
    public chess.ChessBoard getBoard() {
        return myBoard; //return null;
    }

//    public static void main(String[] args) throws InvalidMoveException {
//        chess.ChessGame game;
//        ChessBoard board;
//
//        game = TestFactory.getNewGame();
//        board = TestFactory.getNewBoard();
//
//        /*
//        |k| | | | | | | |
//		| | | | | | | |r|
//		| | | | | | | | |
//		| | | | |q| | | |
//		| | | |n| | |K| |
//		| | | | | | | | |
//		| | | | | | | | |
//		| | | | |b| | | |
//         */
//
//
//        //white king
//        board.addPiece(TestFactory.getNewPosition(4, 7),
//                TestFactory.getNewPiece(TeamColor.WHITE, chess.ChessPiece.PieceType.KING));
//
//        //black king
//        board.addPiece(TestFactory.getNewPosition(8, 1),
//                TestFactory.getNewPiece(TeamColor.BLACK, chess.ChessPiece.PieceType.KING));
//
//        //pieces pinning king
//        board.addPiece(TestFactory.getNewPosition(4, 4),
//                TestFactory.getNewPiece(TeamColor.BLACK, chess.ChessPiece.PieceType.KNIGHT));
//        board.addPiece(TestFactory.getNewPosition(1, 5),
//                TestFactory.getNewPiece(TeamColor.BLACK, chess.ChessPiece.PieceType.BISHOP));
//        board.addPiece(TestFactory.getNewPosition(5, 5),
//                TestFactory.getNewPiece(TeamColor.BLACK, chess.ChessPiece.PieceType.QUEEN));
//        board.addPiece(TestFactory.getNewPosition(7, 8),
//                TestFactory.getNewPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//
//        //set up game
//        game = TestFactory.getNewGame();
//        game.setBoard(board);
//        game.setTeamTurn(TeamColor.WHITE);
//
//        Assertions.assertTrue(game.isInStalemate(TeamColor.WHITE),
//                "White is in a stalemate but isInStalemate returned false");
//
//    }
}
