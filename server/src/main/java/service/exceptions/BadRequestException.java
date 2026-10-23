package service.exceptions;
import dataaccess.DataAccessException;

public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super("bad request");
    }
}
