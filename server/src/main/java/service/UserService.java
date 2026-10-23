package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final AuthService authService;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.authService = new AuthService(authDAO);
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (req == null) { throw new BadRequestException("bad request"); }
        if (isBlank(req.username()) || isBlank(req.password()) || isBlank(req.email())) { throw new BadRequestException("bad request"); }
        if (userDAO.getUser(req.username()) != null) { throw new AlreadyTakenException("already taken"); }

        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));

        String token = generateToken();
        authDAO.createAuth(new AuthData(token, req.username()));
        return new RegisterResult(req.username(), token);
    }

    public LoginResult login(LoginRequest req) throws DataAccessException {
        if (req == null) { throw new BadRequestException("bad request"); }
        if (isBlank(req.username()) || isBlank(req.password())) { throw new BadRequestException("bad request"); }
        UserData user = userDAO.getUser(req.username());
        if (user == null) { throw new UnauthorizedException("unauthorized"); }
        // check password
        if (!req.password().equals(user.password())) { throw new UnauthorizedException("unauthorized"); }

        String token = generateToken();
        authDAO.createAuth(new AuthData(token, req.username()));
        return new LoginResult(req.username(), token);
    }

    public void logout(LogoutRequest req) throws DataAccessException {
        authService.verifyAuth(req.authToken());
        authDAO.deleteAuth(req.authToken());
    }

    private boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
