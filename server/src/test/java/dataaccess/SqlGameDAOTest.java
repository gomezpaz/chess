package dataaccess;

import chess.ChessGame;
import dataaccess.sql.SqlAuthDAO;
import dataaccess.sql.SqlGameDAO;
import dataaccess.sql.SqlUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.*;
import service.exceptions.AlreadyTakenException;

import java.util.Collection;

public class SqlGameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SqlGameDAO();
        gameDAO.clearGames();
    }

    @Test
    @DisplayName("Game Creation Successful")
    public void gameCreationSuccessful() throws DataAccessException {
        int id = gameDAO.createGame("game name");
        Assertions.assertNotNull(id);
        Assertions.assertTrue(id != 0);
    }

    @Test
    @DisplayName("Game Creation Unsuccessful")
    public void gameCreationUnsuccessful() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() throws DataAccessException {
        int id = gameDAO.createGame("game name");
        GameData game = gameDAO.getGame(id);
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("List More Than One Game")
    public void listTwoGames() throws DataAccessException {
        gameDAO.createGame("game name");
        gameDAO.createGame("another name");
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertTrue(games.size() > 1);
    }

    @Test
    @DisplayName("Update Game Successful")
    public void updateGameSuccessful() throws DataAccessException {
        int id = gameDAO.createGame("game name");
        gameDAO.updateGame(new GameData(id, "w", "b", "game name", null));
        GameData game = gameDAO.getGame(id);
        Assertions.assertNotNull(game);
        Assertions.assertEquals(game.whiteUsername(), "w");
        Assertions.assertEquals(game.blackUsername(), "b");
        Assertions.assertEquals(game.gameName(), "game name");
    }
}
