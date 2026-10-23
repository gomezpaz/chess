package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(authDAO, gameDAO);
    }

    @Test
    @DisplayName("Game Creation Successful")
    public void gameCreationSuccessful() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(
                new CreateGameRequest("auth token", "game name")
        );
        int id = createGameResult.gameID();
        Assertions.assertNotNull(gameDAO.getGame(id));
        Assertions.assertTrue(id != 0);
    }
}
