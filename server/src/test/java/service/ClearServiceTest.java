package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ClearService clearService;

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
