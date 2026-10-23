package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        final int id = nextId++;
        games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
        return id;
    }

    @Override
    public GameData getGame(int gameID) { return games.get(gameID); }

    @Override
    public Collection<GameData> listGames() { return new ArrayList<>(games.values()); }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        // If a user attempts to update a non-existent game throw exception
        if (!games.containsKey(game.gameID())) { throw new DataAccessException("game not found"); }
        games.put(game.gameID(), game);
    }

    @Override
    public void clearGames() { games.clear(); }
}