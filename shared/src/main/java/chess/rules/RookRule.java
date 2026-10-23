package chess.rules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookRule implements BaseMovementRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

        int[][] moveVectors = {
                {+1, +0}, // up
                {-1, +0}, // down
                {+0, +1}, // right
                {+0, -1}  // left
        };
        addAllMovesInVectors(board, myPosition, row, col, myTeamColor, moves, moveVectors);

        return moves;
    }
}
