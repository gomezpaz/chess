package chess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public Collection<ChessPosition> getPositions(ChessGame.TeamColor color) {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = squares[i][j];
                if (piece != null) {
                    ChessGame.TeamColor pieceColor = piece.getTeamColor();
                    if (pieceColor == color) positions.add(new ChessPosition(i+1, j+1));
                }
            }
        }

        return positions;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = squares[i][j];
                if (piece != null) {
                    ChessGame.TeamColor pieceColor = piece.getTeamColor();
                    if (pieceColor == color && piece.getPieceType() == ChessPiece.PieceType.KING) {
                        return new ChessPosition(i+1, j+1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // clear board
        for (ChessPiece[] row: squares) {
            Arrays.fill(row, null);
        }

        // fill board
        final ChessPiece.PieceType[] firstRowOrder = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
        };
        for (int i = 0; i < 8; i++) {
            ChessGame.TeamColor teamColor = i <= 1 ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            for (int j = 0; j < 8; j++) {
                if (i == 0 || i == 7) {
                    squares[i][j] = new ChessPiece(teamColor, firstRowOrder[j]);
                }
                if (i == 1 || i == 6) {
                    squares[i][j] = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 7; i >= 0; i--) {
            s += "\n";
            for (int j = 0; j < 8; j++) {
                s += "|";
                ChessPiece piece = squares[i][j];
                if (piece ==  null) {
                    s += " ";
                } else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        s += piece.toString().charAt(0);
                    } else {
                        s += piece.toString().toLowerCase().charAt(0);
                    }
                }
            }
        }
        return s;
    }


}
