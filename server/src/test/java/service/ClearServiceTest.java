package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static ClearService clearService;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(userDAO, authDAO, gameDAO);
    }

    @Test
    @DisplayName("Clear Removes All")
    public void clearRemovesAll() throws DataAccessException {
        userDAO.createUser(new UserData("u","p","e"));
        authDAO.createAuth(new AuthData("tok","u"));
        gameDAO.createGame("g");
        clearService.clear();

        Assertions.assertNull(userDAO.getUser("u"));
        Assertions.assertNull(authDAO.getAuth("tok"));
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }
}
