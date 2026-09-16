package chess;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (ChessPiece[] row: squares) {
            Arrays.fill(row, null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                if (squares[i][j] == null && that.squares[i][j] != null) return false;
                if (squares[i][j] == null && that.squares[i][j] == null) continue;
                if (!squares[i][j].equals(that.squares[i][j])) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 1; i < 8; i++) {
            s += "\n";
            for (int j = 1; j < 8; j++) {
                s += "|";
                ChessPiece piece = squares[i][j];
                if (piece ==  null) {
                    s += " ";
                } else {
                    s += piece.toString().charAt(0);
                }
                s += "|";
            }
        }
        return s;
    }


}
