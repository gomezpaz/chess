package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;
import service.exceptions.BadRequestException;

public class GameHandler {
    private final GameService gameService;
    private final Gson serializer = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(Context ctx) throws DataAccessException {
        CreateGameRequest body = serializer.fromJson(ctx.body(), CreateGameRequest.class);
        if (body == null) {
            throw new BadRequestException("bad request");
        }
        CreateGameRequest req = new CreateGameRequest(ctx.header("authorization"), body.gameName());
        CreateGameResult result = gameService.createGame(req);
        ctx.status(200);
        ctx.result(serializer.toJson(result));

    }

    public void listGames(Context ctx) throws DataAccessException {
        ListGamesRequest req = new ListGamesRequest(ctx.header("authorization"));
        ListGamesResult result = gameService.listGames(req);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
    }

    public void joinGame(Context ctx) throws DataAccessException {
        JoinGameRequest body = serializer.fromJson(ctx.body(), JoinGameRequest.class);
        if (body == null) {
            throw new BadRequestException("bad request");
        }
        JoinGameRequest req = new JoinGameRequest(
                ctx.header("authorization"),
                body.playerColor(),
                body.gameID()
        );
        gameService.joinGame(req);
        ctx.status(200);
        ctx.result("{}");
    }
}
