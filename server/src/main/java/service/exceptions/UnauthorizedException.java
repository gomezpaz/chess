package service.exceptions;
import dataaccess.DataAccessException;

public class UnauthorizedException extends DataAccessException {
    public UnauthorizedException(String message) {
        super("unauthorized");
    }
}
