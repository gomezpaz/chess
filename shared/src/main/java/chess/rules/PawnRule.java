package chess.rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnRule implements BaseMovementRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

        // White moves up, black move down
        int direction = myTeamColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        boolean isInitialMove =
                myTeamColor == ChessGame.TeamColor.WHITE && row == 2 ||
                        myTeamColor == ChessGame.TeamColor.BLACK && row == 7;
        int nextRow = row + direction;
        ChessPiece.PieceType[] promotionPieces = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
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

        return moves;
    }
}
