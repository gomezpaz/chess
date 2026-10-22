package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static UserService userService;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Register Successful")
    public void registerSuccessful() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("u","p","e"));
        Assertions.assertEquals("u", registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotNull(userDAO.getUser("u"));
    }

    @Test
    @DisplayName("Register Missed A Field")
    public void registerMissedAField() throws DataAccessException {
        Assertions.assertThrows(BadRequestException.class, () -> userService.register(
                new RegisterRequest("u", null, "e")
        ));
    }

    @Test
    @DisplayName("Register Duplicate")
    public void registerDuplicate() throws DataAccessException {
        userService.register(new RegisterRequest("u","p","e"));
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(
                new RegisterRequest("u","p2","e2")
        ));
    }
}
