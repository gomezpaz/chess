package dataaccess;

import dataaccess.sql.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.*;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

public class SqlUserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SqlUserDAO();
        userDAO.clearUsers();
    }

    @Test
    @DisplayName("Create User Successful")
    public void createUserSuccessful() throws DataAccessException {
        userDAO.createUser(new UserData("u", "p", "e"));

        UserData stored = userDAO.getUser("u");
        Assertions.assertNotNull(stored);
        Assertions.assertEquals("u", stored.username());
        Assertions.assertEquals("p", stored.password());
        Assertions.assertEquals("e", stored.email());
    }

    @Test
    @DisplayName("Create User Duplicate")
    public void createUserDuplicate() throws DataAccessException {
        userDAO.createUser(new UserData("u", "p", "e"));

        Assertions.assertThrows(DataAccessException.class, () ->
                userDAO.createUser(new UserData("u", "p", "e"))
        );
    }

    @Test
    @DisplayName("Get User Successful")
    public void getUserSuccessful() throws DataAccessException {
        userDAO.createUser(new UserData("u", "p", "e"));
        UserData user = userDAO.getUser("u");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("e", user.email());
        Assertions.assertEquals("p", user.password());
    }

    @Test
    @DisplayName("Get User Not Found")
    public void getUserNotFound() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser("s"));
    }

    @Test
    @DisplayName("Clear Users")
    public void clearUsers() throws DataAccessException {
        userDAO.createUser(new UserData("a", "p", "e"));
        userDAO.createUser(new UserData("b", "p", "e"));

        userDAO.clearUsers();

        Assertions.assertNull(userDAO.getUser("a"));
        Assertions.assertNull(userDAO.getUser("b"));
    }

}
