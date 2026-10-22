package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (req == null) throw new BadRequestException("bad request");
        if (isBlank(req.username()) || isBlank(req.password()) || isBlank(req.email())) {
            throw new BadRequestException("bad request");
        }
        if (userDAO.getUser(req.username()) != null) throw new AlreadyTakenException("already taken");

        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));
        String token = generateToken();
        authDAO.createAuth(new AuthData(token, req.username()));
        return new RegisterResult(req.username(), token);
    }

    private boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
