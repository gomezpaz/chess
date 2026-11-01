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
    private AuthDAO authDAO;
    private GameService gameService;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SqlGameDAO();
        gameDAO.clearGames();
        authDAO = new SqlAuthDAO();
        authDAO.clearAuths();
        gameService = new GameService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("auth token","u"));
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

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(
                new CreateGameRequest("auth token", "game name")
        );
        int id = createGameResult.gameID();
        GameData game = gameDAO.getGame(id);
        ListGamesResult listGamesResult = gameService.listGames(
                new ListGamesRequest("auth token")
        );
        Collection<GameData> games = listGamesResult.games();
        Assertions.assertNotNull(games);
        Assertions.assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("Join Game Successful")
    public void joinGameSuccessful() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(
                new CreateGameRequest("auth token", "game name")
        );
        int id = createGameResult.gameID();
        gameService.joinGame(
                new JoinGameRequest("auth token", ChessGame.TeamColor.WHITE, id)
        );
        GameData game = gameDAO.getGame(id);
        String username = authDAO.getAuth("auth token").username();
        Assertions.assertNotNull(game);
        Assertions.assertSame(game.whiteUsername(), username);
    }

    @Test
    @DisplayName("White Already Taken")
    public void whiteAlreadyTaken() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(
                new CreateGameRequest("auth token", "game name")
        );
        int id = createGameResult.gameID();
        gameService.joinGame(
                new JoinGameRequest("auth token", ChessGame.TeamColor.WHITE, id)
        );
        authDAO.createAuth(new AuthData("another token","s"));
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(
                new JoinGameRequest("another token", ChessGame.TeamColor.WHITE, id)
        ));
    }

    @Test
    @DisplayName("Join Game As Black")
    public void joinGameAsBlack() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(
                new CreateGameRequest("auth token", "game name")
        );
        int id = createGameResult.gameID();
        gameService.joinGame(
                new JoinGameRequest("auth token", ChessGame.TeamColor.BLACK, id)
        );
        GameData game = gameDAO.getGame(id);
        String username = authDAO.getAuth("auth token").username();
        Assertions.assertNotNull(game);
        Assertions.assertSame(game.blackUsername(), username);
    }
}
