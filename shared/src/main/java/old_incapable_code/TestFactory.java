package old_incapable_code;

import java.util.HashSet;
import java.util.Set;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){
        // FIXME
		//return null;
        return new board();
    }

    public static Game getNewGame(){
        // FIXME
		//return null;
        return new Game();
    }

    public static Piece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        // FIXME
		//return null;
        Piece candidatePiece = new Piece();
        candidatePiece.thisPieceType = type;
        candidatePiece.thisTeamColor = pieceColor;
        return candidatePiece;
    }

    public static Position getNewPosition(Integer row, Integer col){ // was ChessPosition
        // FIXME
		//return null;
        return new Position( row, col);
    }

    public static Move getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
        // FIXME // was ChessMove
		//return null;
        Move potentialMove = new Move( startPosition , endPosition );
//        piece differentClassPromotionalPiece = new piece();
//        differentClassPromotionalPiece.thisPieceType = promotionPiece;
//        potentialMove.promotionPiece = differentClassPromotionalPiece;
        potentialMove.promotionPiece = promotionPiece;
        return potentialMove;
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args){
        //System.out.println("hi there");
        ChessBoard board;
        ChessPiece queen;
        ChessPosition position;
        Set<ChessMove> validMoves;

        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();

        queen = getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        position = getNewPosition(7, 7);
        //System.out.println( queen.getTeamColor() );
        board.addPiece(position, queen);
//
//        //down
        validMoves.add(getNewMove(position, getNewPosition(7, 6), null));
        validMoves.add(getNewMove(position, getNewPosition(7, 5), null));
        validMoves.add(getNewMove(position, getNewPosition(7, 4), null));
        validMoves.add(getNewMove(position, getNewPosition(7, 3), null));
        validMoves.add(getNewMove(position, getNewPosition(7, 2), null));
        validMoves.add(getNewMove(position, getNewPosition(7, 1), null));

    }
}
