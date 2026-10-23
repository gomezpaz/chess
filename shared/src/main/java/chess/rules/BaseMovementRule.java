package chess.rules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;

public interface BaseMovementRule {
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);

    /**
     * It adds all potential moves in a certain vector / direction
     * it stops when it reaches one of my pieces or the edge,
     * or eats one of my opponent's pieces and then stops,
     *
     */
    default void addAllMovesInVectors(
            ChessBoard board,
            ChessPosition myPosition,
            int row,
            int col,
            ChessGame.TeamColor myTeamColor,
            List<ChessMove> moves,
            int[][] moveVectors) {
        for (int[] moveVector : moveVectors) {
            for (int i = 1; i < 8; i++) {
                ChessPosition endPosition = new ChessPosition(row + i * moveVector[0], col + i * moveVector[1]);
                if (!endPosition.isValid() || endPosition.isOccupiedByMe(board, myTeamColor)) { break; }
                moves.add(new ChessMove(myPosition, endPosition, null));
                if (endPosition.isOccupiedByOpponent(board, myTeamColor)) { break; }
            }
        }
    }
}
