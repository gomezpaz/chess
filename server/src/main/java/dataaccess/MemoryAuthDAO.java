package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuths() throws DataAccessException {

    }
}
