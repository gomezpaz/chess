package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

        switch (this.type) {
            case BISHOP -> {
                int[][] moveVectors = {
                        {+1, +1}, // up right
                        {+1, -1}, // up left
                        {-1, +1}, // bottom right
                        {-1, -1}  // bottom left
                };

                for (int[] moveVector : moveVectors) {
                    for (int i = 1; i < 7; i++) {
                        ChessPosition endPosition = new ChessPosition(row + i * moveVector[0], col + i * moveVector[1]);
                        if (!endPosition.isValid() || endPosition.isOccupiedByMe(board, myTeamColor)) break;
                        moves.add(new ChessMove(myPosition, endPosition, null));
                        if (endPosition.isOccupiedByOpponent(board, myTeamColor)) break;
                    }
                }
            }
            case KING -> {
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        ChessPosition endPosition = new ChessPosition(i, j);
                        if (endPosition.isValid() && !endPosition.isOccupiedByMe(board, myTeamColor)) {
                            moves.add(new ChessMove(myPosition, endPosition,null));
                        }
                    }
                }
            }
            case KNIGHT -> {
                int[][] relativeMoves = {
                        {-2, -1},
                        {-2, +1},
                        {+2, -1},
                        {+2, +1},
                        {-1, -2},
                        {-1, +2},
                        {+1, -2},
                        {+1, +2}
                };
                for (int [] relativeMove : relativeMoves) {
                    ChessPosition endPosition = new ChessPosition(
                            row + relativeMove[0],
                            col + relativeMove[1]
                    );
                    if (endPosition.isValid() && !endPosition.isOccupiedByMe(board, myTeamColor)) {
                        moves.add(new ChessMove(myPosition, endPosition,null));
                    }
                }
            }
            case PAWN -> {
                // White moves up, black move down
                int direction = myTeamColor == ChessGame.TeamColor.WHITE ? 1 : -1;
                boolean isInitialMove = 
                        myTeamColor == ChessGame.TeamColor.WHITE && row == 2 ||
                        myTeamColor == ChessGame.TeamColor.BLACK && row == 7;
                int nextRow = row + direction;
                ChessPiece.PieceType[] promotionPieces = {
                        PieceType.QUEEN,
                        PieceType.BISHOP,
                        PieceType.KNIGHT,
                        PieceType.ROOK
                };
                
                // Move vertically
                ChessPosition nextVerticalPosition = new ChessPosition(nextRow, col);
                if (!nextVerticalPosition.isOccupied(board)) {
                    if (nextRow == 1 || nextRow == 8) {
                        // If end of the board, can pick a promotion piece
                        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                            moves.add(new ChessMove(myPosition, nextVerticalPosition, promotionPiece));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, nextVerticalPosition, null));
                    }

                    // If initial move, can also move two vertically
                    ChessPosition nextNextVerticalPosition = new ChessPosition(row + 2 * direction, col);
                    if (isInitialMove && !nextNextVerticalPosition.isOccupied(board)) {
                        moves.add(new ChessMove(myPosition, nextNextVerticalPosition, null));
                    }
                }
                
                // Move diagonally
                ChessPosition[] diagonalPositions = {
                        new ChessPosition(nextRow, col - 1),
                        new ChessPosition(nextRow, col + 1)
                };
                for (ChessPosition diagonalPosition : diagonalPositions) {
                    if (diagonalPosition.isValid() && diagonalPosition.isOccupiedByOpponent(board, myTeamColor)) {
                        if (nextRow == 1 || nextRow == 8) {
                            // If end of the board, can pick a promotion piece
                            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, diagonalPosition, promotionPiece));
                            }
                        } else {
                            moves.add(new ChessMove(myPosition, diagonalPosition, null));
                        }
                    }
                }
            }
            case QUEEN -> {

            }
        }

        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "{" + pieceColor + "," + type + "}";
    }
}
