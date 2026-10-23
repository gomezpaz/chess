package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

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

    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("u","p","e"));
        String token = registerResult.authToken();
        userService.logout(new LogoutRequest(token));
        LoginResult loginResult = userService.login(new LoginRequest("u","p"));
        Assertions.assertNotNull(loginResult.authToken());
        Assertions.assertNotEquals(registerResult.authToken(), loginResult.authToken());
    }

    @Test
    @DisplayName("Login Unsuccessful")
    public void loginUnsuccessful() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(
                new LoginRequest("santi","p")
        ));
    }

    @Test
    @DisplayName("Bad Password Login")
    public void badPasswordLogin() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("u","p","e"));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(
                new LoginRequest("u","another password")
        ));
    }

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccessful() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("u","p","e"));
        String token = registerResult.authToken();
        userService.logout(new LogoutRequest(token));
        Assertions.assertNull(authDAO.getAuth(token));
    }
}
