package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData verifyAuth(String token) throws DataAccessException {
        if (token == null) {
            throw new UnauthorizedException("unauthorized");
        }
        AuthData auth = authDAO.getAuth(token);
        if (auth == null) {
            throw new UnauthorizedException("unauthorized");
        } else {
            return auth;
        }
    }
}
