package chess.rules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingRule implements BaseMovementRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();
        List<ChessMove> moves = new ArrayList<>();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                ChessPosition endPosition = new ChessPosition(i, j);
                if (endPosition.isValid() && !endPosition.isOccupiedByMe(board, myTeamColor)) {
                    moves.add(new ChessMove(myPosition, endPosition,null));
                }
            }
        }

        return moves;
    }
}
