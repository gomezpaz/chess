package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardRenderer {
    public void draw(ChessBoard board, boolean whiteAtBottom) {
        printColumns(whiteAtBottom);
        for (int i = 0; i < 8; i++) {
            int row = whiteAtBottom ? 8 - i : i + 1;
            System.out.print(" " + row + " ");
            for (int j = 0; j < 8; j++) {
                int col = whiteAtBottom ? j + 1 : 8 - j;
                printSquare(board, row, col);
            }
            System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + " " + row);
        }
        printColumns(whiteAtBottom);
    }

    private void printColumns(boolean whiteAtBottom) {
        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            char col = (char) (whiteAtBottom ? 'a' + i : 'h' - i);
            System.out.print(" " + col + " ");
        }
        System.out.println();
    }

    private void printSquare(ChessBoard board, int row, int col) {
        boolean dark = (row + col) % 2 == 0;
        System.out.print(dark ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY);
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            boolean white = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
            System.out.print(white ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE);
            System.out.print(getSymbol(piece));
        }
    }

    private String getSymbol(ChessPiece piece) {
        boolean white = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING -> white ? WHITE_KING : BLACK_KING;
            case QUEEN -> white ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> white ? WHITE_ROOK : BLACK_ROOK;
            case BISHOP -> white ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> white ? WHITE_KNIGHT : BLACK_KNIGHT;
            case PAWN -> white ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}