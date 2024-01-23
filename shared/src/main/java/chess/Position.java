package chess;

import chess.ChessPosition;

public class Position extends ChessPosition {
    public int row = 0;
    public int column = 0;
    public Position(int roww, int coll){
        super( roww, coll);
        row = roww;
        column = coll;
    }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("            got to 0");
        if (this == obj) {
            return true; // Same reference
        }
        System.out.println("            got to 1");

        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }
        System.out.println("            got to 2");

        Position otherPosition = (Position) obj; // Cast obj to Person
        return otherPosition.getRow() == getRow() && otherPosition.getColumn() == getColumn();
    }

    public int hashCode() {
        int result = 17;
        result += 31 * result + row;
        result += 31 * result + column;
        return result;
    }
}
