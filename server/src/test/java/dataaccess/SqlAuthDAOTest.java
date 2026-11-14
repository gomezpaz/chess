package dataaccess;

import dataaccess.sql.SqlAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SqlAuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SqlAuthDAO();
        authDAO.clearAuths();
    }

    @Test
    @DisplayName("Create Auth Successful")
    public void createAuthSuccessful() throws DataAccessException {
        authDAO.createAuth(new AuthData("t", "u"));

        AuthData stored = authDAO.getAuth("t");
        Assertions.assertNotNull(stored);
        Assertions.assertEquals("t", stored.authToken());
    }

    @Test
    @DisplayName("Create Auth Duplicate")
    public void createAuthDuplicate() throws DataAccessException {
        authDAO.createAuth(new AuthData("t", "u"));

        Assertions.assertThrows(DataAccessException.class, () ->
                authDAO.createAuth(new AuthData("t", "u"))
        );
    }

    @Test
    @DisplayName("Get Auth Successful")
    public void getAuthSuccessful() throws DataAccessException {
        authDAO.createAuth(new AuthData("t", "u"));
        AuthData auth = authDAO.getAuth("t");

        Assertions.assertNotNull(auth);
        Assertions.assertEquals("u", auth.username());
    }

    @Test
    @DisplayName("Get Auth Not Found")
    public void getAuthNotFound() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth("s"));
    }

    @Test
    @DisplayName("Delete Auth Successful")
    public void deleteAuthSuccessful() throws DataAccessException {
        authDAO.createAuth(new AuthData("t", "u"));
        authDAO.deleteAuth("t");

        Assertions.assertNull(authDAO.getAuth("t"));
    }

    @Test
    @DisplayName("Delete Auth Not Found")
    public void deleteAuthNotFound() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("s"));
    }

    @Test
    @DisplayName("Clear Auths")
    public void clearAuths() throws DataAccessException {
        authDAO.createAuth(new AuthData("a", "a"));
        authDAO.createAuth(new AuthData("b", "b"));

        authDAO.clearAuths();

        Assertions.assertNull(authDAO.getAuth("a"));
        Assertions.assertNull(authDAO.getAuth("b"));
    }

}
