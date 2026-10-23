package chess.rules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopRule implements BaseMovementRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

        int[][] moveVectors = {
                {+1, +1}, // up right
                {+1, -1}, // up left
                {-1, +1}, // bottom right
                {-1, -1}  // bottom left
        };

        addAllMovesInVectors(board, myPosition, row, col, myTeamColor, moves, moveVectors);

        return moves;
    }
}
