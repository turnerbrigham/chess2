package chess;

public class ChessMove extends ChessMoveOrig {
    ChessPositionOrig startPosition;
    ChessPositionOrig endPosition;
    public ChessPieceOrig.PieceType promotionPiece;

    public static ChessMove convertToHigher(ChessMoveOrig oldMove ){
        //Move newMove = new Move( oldMove.getStartPosition() , oldMove.getEndPosition()  ) ;
        //newMove.promotionPiece = oldMove.getPromotionPiece() ;
        //return newMove;
        return new ChessMove( oldMove.getStartPosition() , oldMove.getEndPosition() , oldMove.getPromotionPiece() );
    }

    public ChessMove(ChessPositionOrig startt, ChessPositionOrig endd , ChessPieceOrig.PieceType promotionPiece ){
        //ChessPosition startPosition, ChessPosition endPosition,
        //                     ChessPiece.PieceType promotionPiece
        super( startt, endd, promotionPiece );
        startPosition = startt;
        endPosition = endd;
        this.promotionPiece = promotionPiece;
    }
    public ChessMove(ChessPositionOrig startt, ChessPositionOrig endd ){
        //ChessPosition startPosition, ChessPosition endPosition,
        //                     ChessPiece.PieceType promotionPiece
        super( startt, endd, null );
        startPosition = startt;
        endPosition = endd;
    }
    @Override
    public ChessPositionOrig getStartPosition() {
        return startPosition; //return null;
    }

    @Override
    public ChessPositionOrig getEndPosition() {
        return endPosition; //return null;
    }

    @Override
    public ChessPieceOrig.PieceType getPromotionPiece() {
//        if ( promotionPiece==null){
//            return null;
//        }
        return promotionPiece;  //.thisPieceType; //return null;
    }
    @Override
    public boolean equals(Object obj) {
        System.out.println("no yeah");
        if (this == obj) {
            return true; // Same reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }

        ChessMove otherMove = (ChessMove) obj; // Cast obj to Person
        System.out.println("no yeah" + (otherMove.promotionPiece == promotionPiece && otherMove.startPosition == startPosition && otherMove.endPosition == endPosition) );
        System.out.println("no yeah1" + (otherMove.promotionPiece == promotionPiece ) );
        System.out.println("no yeah2" + (otherMove.startPosition == startPosition ) );
        System.out.println("no yeah3" + (otherMove.endPosition == endPosition) );
        System.out.println("no yeah gh " + (otherMove.endPosition.getRow() ) + "_" + (otherMove.endPosition.getColumn() )  );
        System.out.println("no yeah gh " + (  endPosition.getRow() ) + "_" + (  endPosition.getColumn() )  );

        //return otherMove.promotionPiece == promotionPiece && otherMove.startPosition == startPosition && otherMove.endPosition == endPosition;
        return otherMove.promotionPiece == promotionPiece &&
                otherMove.startPosition.getRow() == startPosition.getRow() &&
                otherMove.startPosition.getColumn() == startPosition.getColumn() &&
                otherMove.endPosition.getRow() == endPosition.getRow() &&
                otherMove.endPosition.getColumn() == endPosition.getColumn();

    }
    //@Override
    public int hashCode() {
        // Combine the hash codes of relevant fields
        //System.out.println( "sad why" );
        int Starthash = startPosition.getRow()*10000+startPosition.getColumn()*1000+endPosition.getRow()*100+endPosition.getColumn()*10;
//        System.out.println( Starthash );
//        System.out.println( startPosition.getRow() );
        if (promotionPiece==null){
            Starthash -= 1;
        }
        else{
            Starthash += promotionPiece.ordinal();
        }
        return Starthash;
    }
//    public static void main(String[] args){
//        Move move1 = new Move( new Position(5,5), new Position(6,6) );
//        Move move2 = new Move( new Position(3,3), new Position(4,4) );
//        System.out.println( "checking move equality:"+(move1==move2));
//        System.out.println( move1.hashCode() );
//        System.out.println( move2.hashCode() );
//    }
}
