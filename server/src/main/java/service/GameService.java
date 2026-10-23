package service;

import dataaccess.*;

public class GameService {
    private final AuthService authService;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authService = new AuthService(authDAO);
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException {
        authService.verifyAuth(req.authToken());
        if (isBlank(req.gameName())) {
            throw new BadRequestException("bad request");
        }
        int id = gameDAO.createGame(req.gameName());
        return new CreateGameResult(id);
    }

    private boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }
}
