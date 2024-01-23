package chess;

import java.util.*;

public class ChessGame extends ChessGameOrig {
    public TeamColor myTurn = TeamColor.WHITE;
    public ChessBoard myBoard = new ChessBoard();
    public ChessPosition enPassantOpportunity = null;
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
    public ChessGame getCopy(){
        ChessGame newGame = new ChessGame();
        newGame.myTurn = myTurn;
        newGame.myBoard = myBoard.getCopy();

        newGame.castleLeftAllowed_white = castleLeftAllowed_white;
        newGame.castleRightAllowed_white = castleRightAllowed_white;
        newGame.castleLeftAllowed_black = castleLeftAllowed_black;
        newGame.castleRightAllowed_black = castleRightAllowed_black;

        return newGame;
    }
    public void forceMove(ChessMoveOrig movee) {
        // what does this ?
        ChessMove move2 = ChessMove.convertToHigher(movee);
        ChessPiece pieceCopy = ChessPiece.convertToHigher(myBoard.getPiece(move2.getStartPosition()));
        if (movee.getPromotionPiece() != null) {
            pieceCopy.thisPieceType = move2.promotionPiece;
        }

        //killing enemy because of en passant:
        if (pieceCopy.thisPieceType == ChessPieceOrig.PieceType.PAWN) {
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
        if (pieceCopy.thisPieceType == ChessPieceOrig.PieceType.PAWN &&
                (rowMovement == 2 || rowMovement == -2)) {
            enPassantOpportunity = new ChessPosition(move2.getEndPosition().getRow(), move2.getEndPosition().getColumn());
        } else {
            enPassantOpportunity = null;
        }

        myBoard.addPiece(move2.getEndPosition(), pieceCopy); //myBoard.getPiece(move2.getStartPosition()) );
        myBoard.deletePiece(move2.getStartPosition());

        int sRow = move2.getStartPosition().getRow();
        int sCol = move2.getStartPosition().getColumn();
        int eRow = move2.getEndPosition().getRow();
        int eCol = move2.getEndPosition().getColumn();

        if ( pieceCopy.thisPieceType == ChessPieceOrig.PieceType.KING ) {
            if ( pieceCopy.thisTeamColor == TeamColor.WHITE ) {
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 3 && castleLeftAllowed_white) {
                    forceMove(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 1 && sCol == 5 &&
                        eRow == 1 && eCol == 7 && castleRightAllowed_white) {
                    forceMove(new ChessMove(new ChessPosition(1, 8), new ChessPosition(1, 6) , movee.getPromotionPiece() ));
                    swapTurn();
                }
            }
            if ( pieceCopy.thisTeamColor == TeamColor.BLACK ) {
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 3 && castleLeftAllowed_black) {
                    forceMove(new ChessMove(new ChessPosition(8, 1), new ChessPosition(8, 4) , movee.getPromotionPiece() ));
                    swapTurn();
                }
                if (sRow == 8 && sCol == 5 &&
                        eRow == 8 && eCol == 7 && castleRightAllowed_black) {
                    forceMove(new ChessMove(new ChessPosition(8, 8), new ChessPosition(8, 6) , movee.getPromotionPiece() ));
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
    boolean willThereBeACheck( ChessMoveOrig movee ){
        ChessGame stagedGame = getCopy();
        TeamColor presentColor = stagedGame.getBoard().getPiece( movee.getStartPosition() ).getTeamColor();
        stagedGame.forceMove( movee );
        return stagedGame.isInCheck( presentColor );
    }
    boolean isMeerlyClearOfBlocks( ChessPosition thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return true;
    }
    boolean isClearOfBlocksOrChecks( ChessPosition thisPosition  ){
        if ( myBoard.thisChessBoard[ thisPosition.row ][ thisPosition.column ]!=null ){
            return false;
        }
        return ( !willThereBeACheck( new ChessMove( new ChessPosition(thisPosition.row,5) , thisPosition ) ) );
    }
    boolean isClearOfChecks( ChessPosition thisPosition  ){
        return ( !willThereBeACheck( new ChessMove( new ChessPosition(thisPosition.row,5) , thisPosition ) ) );
    }
    boolean sectionClearOfStuff( ChessPosition thisPosition ){
        int directionn;
        if ( thisPosition.column == 1 ){
            directionn = -1;
        }
        else{
            directionn = 1;
        }
        for ( int col = 5+directionn; col != (5+ 3*directionn); col += directionn ){
            if ( !isClearOfBlocksOrChecks( new ChessPosition( thisPosition.row , col )  ) ){
                return false;
            }
        }
//        if ( !isClearOfChecks( thisPosition  ) ){
//            return false;
//        }
        if ( !isMeerlyClearOfBlocks( new ChessPosition( thisPosition.row, thisPosition.column-directionn ) )){
            return false;
        }

        if ( !isClearOfChecks( new ChessPosition( thisPosition.row , 5 )  ) ){
            return false;
        }
        if ( thisPosition.row == 1 ){
            ChessPieceOrig rookPiece = myBoard.getPiece( new ChessPosition(1,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != ChessPieceOrig.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
            ChessPieceOrig kingPiece = myBoard.getPiece( new ChessPosition(1,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != ChessPieceOrig.PieceType.KING || kingPiece.getTeamColor() != TeamColor.WHITE   ){
                return false;
            }
        }
        if ( thisPosition.row == 8 ){
            ChessPieceOrig rookPiece = myBoard.getPiece( new ChessPosition(8,thisPosition.column ) );
            if ( rookPiece == null  ){
                return false;
            }
            if (  rookPiece.getPieceType() != ChessPieceOrig.PieceType.ROOK || rookPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
            ChessPieceOrig kingPiece = myBoard.getPiece( new ChessPosition(8,5) );
            if ( kingPiece == null ){
                return false;
            }
            if (  kingPiece.getPieceType() != ChessPieceOrig.PieceType.KING || kingPiece.getTeamColor() != TeamColor.BLACK   ){
                return false;
            }
        }

        return true;
    }
    public Collection<ChessMoveOrig> pinOnCastling(ChessPositionOrig startPosition ){
        Set<ChessMoveOrig> emoves;
        emoves = new HashSet<>();
        ChessPieceOrig potentialPiece = myBoard.getPiece( startPosition );
        if (potentialPiece == null){
            return  emoves;
        }
        Collection<ChessMoveOrig> movesFinal = potentialPiece.pieceMoves(  myBoard , startPosition );
        // yay castling!!
        if ( castleLeftAllowed_white && startPosition.getRow()==1 &&
                                        startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 1 , 1) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(1 , 3 ) ) );
            }
        }
        if ( castleRightAllowed_white && startPosition.getRow()==1 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 1 , 8) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(1 , 7 ) ) );
            }
        }
        if ( castleLeftAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){
            //System.out.println(  "      fortun status:" + sectionClearOfStuff( new position( 8 , 1) ) );
            if (sectionClearOfStuff( new ChessPosition( 8 , 1) ) ){
                //System.out.println( "   even got inside ");
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(8 , 3 ) ) );
            }
        }
        //System.out.println( "       giving up:"+castleLeftAllowed_black);
        if ( castleRightAllowed_black && startPosition.getRow()==8 &&
                startPosition.getColumn()==5 ){
            if (sectionClearOfStuff( new ChessPosition( 8 , 8) ) ){
                movesFinal.add( new ChessMove(  startPosition , new ChessPosition(8 , 7 ) ) );
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
                    movesFinal.add( new ChessMove(  startPosition ,
                       new ChessPosition( startPosition.getRow()+1 , enPassantOpportunity.getColumn() ) ) );
                }
                else {
                    movesFinal.add( new ChessMove(  startPosition ,
                       new ChessPosition(startPosition.getRow()-1 , enPassantOpportunity.getColumn()  ) ) );
                }
            }
        }
        return movesFinal;
    }
    @Override
    public Collection<ChessMoveOrig> validMoves(ChessPositionOrig startPosition) {
        //do care about check, but don't care about being in turn or not
        Set<ChessMoveOrig> emoves;
        emoves = new HashSet<>();
        // first checking if the position is empty
//        ChessPiece potentialPiece = myBoard.getPiece( startPosition );
//        if (potentialPiece == null){
//            return  emoves;
//        }
//        Collection<ChessMove> movesFinal = potentialPiece.p/ieceMoves(  myBoard , startPosition );
        //System.out.println(" getting valid moves");
        Collection<ChessMoveOrig> movesFinal = pinOnCastling( startPosition );
        Iterator<ChessMoveOrig> iterator = movesFinal.iterator();
        while (iterator.hasNext()) {
            ChessMoveOrig presentMove = iterator.next();
            if (willThereBeACheck(presentMove)) {
                iterator.remove();
            }
        }
        return movesFinal;
    }
    public boolean isValidMove(ChessMove movee){
        //don't care about check, but do care about being in turn or not
//        ChessPiece potentialPiece = myBoard.getPiece( movee.getStartPosition() );
//        if (potentialPiece == null){
//            return false;
//        }
//        Collection<ChessMove> myValidMoves = potentialPiece.p/ieceMoves(  myBoard , movee.getStartPosition() );
        //System.out.println("  specifically is valid move");
        Collection<ChessMoveOrig> myValidMoves = pinOnCastling( movee.getStartPosition() );

        if ( myValidMoves == null ){
            return false;
        }
        boolean isInIt = false;
        int endPositionRow    = movee.getEndPosition().getRow();
        int endPositionColumn = movee.getEndPosition().getColumn();
        for ( ChessMoveOrig possibleMove : myValidMoves ) {
            ChessPositionOrig positionFromIter = possibleMove.getEndPosition();
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

    public Collection<ChessMoveOrig> literallyAllPossibleMoves(TeamColor teamColor) {
        Collection<ChessMoveOrig> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ChessPosition pos = new ChessPosition( i, j);
                ChessPieceOrig pieceHere = myBoard.getPiece( pos );
                if ( pieceHere!= null ){
                    if (pieceHere.getTeamColor() == teamColor){
                        //Collection<ChessMove> possibleMovesHere = pieceHere.p/ieceMoves( myBoard , pos );
                        //System.out.println("   getting all possible notes literally");
                        Collection<ChessMoveOrig> possibleMovesHere = pinOnCastling( pos );

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
        Collection<ChessMoveOrig> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( ChessMoveOrig possibleMove : allPossibleMoves ) {
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
        Collection<ChessMoveOrig> allPossibleMoves = literallyAllPossibleMoves( teamColor );
        for ( ChessMoveOrig possibleMove : allPossibleMoves ) {
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
    public void makeMove(ChessMoveOrig movee) throws chess.InvalidMoveException {
        if (  !isValidMove( ChessMove.convertToHigher(movee) )  ){
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
                ChessPieceOrig presentPiece = myBoard.getPiece2(i,j);
                if ( presentPiece != null ){
                    if (presentPiece.getPieceType() == ChessPieceOrig.PieceType.KING &&
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
        Collection<ChessMoveOrig> allMoves = literallyAllPossibleMoves( otherTeamColor );
        for ( ChessMoveOrig possibleMove : allMoves ) {
            if ( possibleMove.getEndPosition().getRow() == kingRow &&
                    possibleMove.getEndPosition().getColumn() == kingCol ){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setBoard(ChessBoardOrig board) {
        ChessPiece[][] newChessBoard = new ChessPiece[9][9];
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPieceOrig oldPiece = board.getPiece( new ChessPosition(i,j) );
                if (oldPiece != null) {
                    //System.out.println("something happened");
                    //Piece newPiece = new Piece();
                    //newPiece.thisPieceType = oldPiece.getPieceType();
                    //newPiece.thisTeamColor = oldPiece.getTeamColor();

                    //public Piece(ChessGame.TeamColor pieceColor, PieceType type) {
                    ChessPiece newPiece = new ChessPiece(oldPiece.getTeamColor() , oldPiece.getPieceType());

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
    public ChessBoardOrig getBoard() {
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
