package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return this.col;
    }

    /**
     *
     * @return true if the position is outside the board (invalid)
     */
    public boolean isInvalid() {
        return !(row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }

    /**
     *
     * @return true if the position is occupied by one of my pieces already
     */
    public boolean isOccupiedByMe(ChessBoard board, ChessGame.TeamColor myTeamColor) {
        ChessPiece piece = board.getPiece(this);
        if (piece == null) return false;
        return piece.getTeamColor() == myTeamColor;
    }

    /**
     *
     * @return true if the position is occupied by one of my opponent's pieces
     */
    public boolean isOccupiedByOpponent(ChessBoard board, ChessGame.TeamColor myTeamColor) {
        ChessPiece piece = board.getPiece(this);
        if (piece == null) return false;
        return piece.getTeamColor() != myTeamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPosition that)) {
            return false;
        }
        return getRow() == that.getRow() && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), col);
    }

    @Override
    public String toString() {
        return "[" + row + "," + col + "]";
    }
}
