package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;

public class GameService {
    private final AuthService authService;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authService = new AuthService(authDAO);
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException {
        authService.verifyAuth(req.authToken());
        if (isBlank(req.gameName())) { throw new BadRequestException("bad request"); }
        int id = gameDAO.createGame(req.gameName());
        return new CreateGameResult(id);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws DataAccessException {
        authService.verifyAuth(req.authToken());
        return new ListGamesResult(gameDAO.listGames());
    }

    public void joinGame(JoinGameRequest req) throws DataAccessException {
        AuthData auth = authService.verifyAuth(req.authToken());
        String username = auth.username();
        if (req.gameID() == null) { throw new BadRequestException("bad request"); }
        ChessGame.TeamColor playerColor = req.playerColor();

        boolean invalidColor = playerColor != ChessGame.TeamColor.BLACK && playerColor != ChessGame.TeamColor.WHITE;
        if (invalidColor) { throw new BadRequestException("bad request"); }

        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) { throw new BadRequestException("bad request"); }

        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        switch(playerColor) {
            case WHITE -> {
                if (game.whiteUsername() != null) { throw new AlreadyTakenException("taken"); }
                whiteUsername = username;
            }
            case BLACK -> {
                if (game.blackUsername() != null) { throw new AlreadyTakenException("taken"); }
                blackUsername = username;
            }
        }

        gameDAO.updateGame(new GameData(
                game.gameID(),
                whiteUsername,
                blackUsername,
                game.gameName(),
                game.game()
        ));
    }

    private boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }
}
