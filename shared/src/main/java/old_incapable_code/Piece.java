package old_incapable_code;

import java.util.*;

public class Piece implements ChessPiece {
    public PieceType thisPieceType = PieceType.ROOK;
    public ChessGame.TeamColor thisTeamColor = ChessGame.TeamColor.WHITE;
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return thisTeamColor; //null;
    }
    public static Piece convertToHigher(ChessPiece oldPiece){
        Piece newPiece = new Piece();
        newPiece.thisPieceType = oldPiece.getPieceType() ;
        newPiece.thisTeamColor = oldPiece.getTeamColor() ;
        return newPiece;
    }
    public Piece getCopy(){
        Piece newPiece = new Piece();
        newPiece.thisPieceType = thisPieceType;
        newPiece.thisTeamColor = thisTeamColor;
        return newPiece;
    }
    @Override
    public PieceType getPieceType() {
        return thisPieceType; //null;
    }
    public boolean myPieceHere( ChessBoard board , Position testPos ){
        if (testPos.row > 8 || testPos.row < 1 ||testPos.column<1 || testPos.column > 8){
            return false;
        }
        if ( board.getPiece( testPos ) != null ){
            //System.out.println("at least a piece is here")
            if ( board.getPiece( testPos ).getTeamColor() == thisTeamColor ){
                return true;
            }
        }
        return false;
    }
    boolean otherPieceHere( ChessBoard board , Position testPos ){
        if (testPos.row > 8 || testPos.row < 1 ||testPos.column<1 || testPos.column >8){
            return false;
        }
        if ( board.getPiece( testPos ) != null ){
            if ( board.getPiece( testPos ).getTeamColor() != thisTeamColor ){
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ) {
        Position startPosition = new Position( myPosition.getRow() , myPosition.getColumn() );
        //Collection<ChessMove> moves = new ArrayList<>();
        Set<ChessMove> moves;
        moves = new HashSet<>();

        if (thisPieceType == PieceType.PAWN){
            PieceType[] allTypes = {PieceType.ROOK,PieceType.KNIGHT,PieceType.BISHOP, PieceType.QUEEN }; //PieceType.PAWN,

            int directionn;
            if ( thisTeamColor == ChessGame.TeamColor.WHITE ){
                directionn = 1;
            }
            else{
                directionn = -1;
            }
            //moving two forward:
            if ( (startPosition.row==2 && directionn == 1)  ||
                    (startPosition.row==7 && directionn == -1) ){
                Position newPosition_sm = new Position( startPosition.row+directionn , startPosition.column );
                Position newPosition = new Position( startPosition.row+ 2*directionn , startPosition.column );

                if ( !(  myPieceHere( board , newPosition ) || otherPieceHere( board , newPosition ) ||
                        myPieceHere( board , newPosition_sm ) || otherPieceHere( board , newPosition_sm )  )){
                    moves.add(new Move( startPosition , newPosition ) );
                }
            }

            //generally moving forward
            //for (int i : new int[]{1,2} ){
            Position newPosition = new Position( startPosition.row+directionn , startPosition.column );
            if (  !(myPieceHere( board , newPosition ) || otherPieceHere( board , newPosition )   )  ){
                //promoting piece if necessary
                if (newPosition.row == 1 || newPosition.row == 8){
                    //System.out.println("maybe promoting normally");
                    for (PieceType samplePiece : allTypes ){
                        //System.out.println("promoting normally");
                        Move potentialMove = new Move( startPosition , newPosition );
    //                        potentialMove.promotionPiece = new piece();
    //                        potentialMove.promotionPiece.thisPieceType = samplePiece;
    //                        potentialMove.promotionPiece.thisTeamColor = thisTeamColor;

                        potentialMove.promotionPiece = samplePiece;
                        moves.add( potentialMove );
                    }
                }
                else {
                    moves.add(new Move( startPosition , newPosition ) );
                }
            }

            //killing someone
            //System.out.println("maybe kill? 1");
            int newRow = startPosition.row + directionn;
            if ( newRow >0 && newRow <= 8 ){
                //System.out.println("maybe kill? 2");
                for (int i : new int[]{-1,1} ){
                    int newColumn = startPosition.column + i;
                    if ( newColumn >0 && newColumn <= 8 ){
                        //System.out.println("maybe kill? 3");
                        Position endPosition = new Position( newRow , newColumn );
                        if (board.getPiece( endPosition ) != null ){
                            //System.out.println("maybe kill? 4");
                            //I can add a new move, but first I should consider promotion
                            if (endPosition.column == 1 || endPosition.column == 8){
                                //System.out.println("maybe kill? 5");
                                for (PieceType samplePiece : allTypes ){
                                    Move potentialMove = new Move( startPosition , endPosition );
//                                    potentialMove.promotionPiece = new piece();
//                                    potentialMove.promotionPiece.thisPieceType = samplePiece;
//                                    potentialMove.promotionPiece.thisTeamColor = thisTeamColor;
                                    potentialMove.promotionPiece = samplePiece;
                                    moves.add( potentialMove );
                                }
                            }
                            else {
                                //System.out.println("maybe kill? 4.5");
                                moves.add(new Move( startPosition , endPosition ) );
                            }
                        }
                    }
                }
            }
        }
        if (thisPieceType == PieceType.ROOK || thisPieceType == PieceType.QUEEN ){
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    Position newPosition = new Position( startPosition.row+i*Qdirection , startPosition.column );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new Move( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    Position newPosition = new Position( startPosition.row , startPosition.column+i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new Move( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
        }
        if (thisPieceType == PieceType.BISHOP || thisPieceType == PieceType.QUEEN ){
            System.out.println( "bishop zone: "  );
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    Position newPosition = new Position( startPosition.row+i*Qdirection , startPosition.column+i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new Move( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
            for (int Qdirection : new int[]{-1,1} ){
                for (int i = 1 ; i < 8 ; i++){
                    Position newPosition = new Position( startPosition.row+i*Qdirection , startPosition.column-i*Qdirection );
                    if (myPieceHere( board , newPosition )){
                        break;
                    }
                    moves.add(new Move( startPosition , newPosition ));
                    if (otherPieceHere( board , newPosition )){
                        break;
                    }
                }
            }
        }
        if (thisPieceType == PieceType.KING ){
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row + 1 , startPosition.column ) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row - 1 , startPosition.column ) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row , startPosition.column + 1 ) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row , startPosition.column - 1 ) ));

            moves.add(new Move( startPosition ,
                    new Position( startPosition.row + 1 , startPosition.column +1) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row - 1 , startPosition.column -1) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row  +1, startPosition.column - 1 ) ));
            moves.add(new Move( startPosition ,
                    new Position( startPosition.row -1 , startPosition.column + 1 ) ));

        }
        if (thisPieceType == PieceType.KNIGHT){
            for (  int Qdirection : new int[]{-2,2} ){
                for (  int side : new int[]{-1,1} ){
                    moves.add(new Move( startPosition ,
                            new Position( startPosition.row+side , startPosition.column + Qdirection ) ));
                    moves.add(new Move( startPosition ,
                            new Position( startPosition.row+Qdirection , startPosition.column + side ) ));
                }
            }
        }
//        System.out.println( "inter3_size: " + moves.size() );
//        System.out.println( "__" + myPosition.getRow() + "_" + myPosition.getColumn() );
//        System.out.println( " I here:" + myPieceHere( board , new position(6,6)) );

        Iterator<ChessMove> iterator = moves.iterator();
        while (iterator.hasNext()) {
            ChessMove presentMove = iterator.next();
            Position endPosition = new Position( presentMove.getEndPosition().getRow() , presentMove.getEndPosition().getColumn() );
            if ( myPieceHere( board , endPosition ) || endPosition.row > 8 || endPosition.row <= 0 ||
                                                       endPosition.column > 8 || endPosition.column <= 0) {
                //System.out.println( "  " + endPosition.row + "_" + endPosition.column );
                iterator.remove();
            }
        }
        //System.out.println( "inter4_size: " + moves.size() );

        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        //System.out.println("fuck yeah");
        if (this == obj) {
            return true; // Same reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }

        Piece otherPiece = (Piece) obj; // Cast obj to Person
        return otherPiece.thisPieceType == thisPieceType && otherPiece.thisTeamColor == thisTeamColor;
    }
    public static void main(String[] args){
        PieceType thisPieceType = PieceType.ROOK;
        PieceType otherPieceType = thisPieceType;
        thisPieceType = PieceType.BISHOP;
        System.out.println( thisPieceType  );
        System.out.println( otherPieceType  );
    }
}
