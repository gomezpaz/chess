package chess.rules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightRule implements BaseMovementRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

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

        return moves;
    }
}
