package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import service.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        // CLEAR
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::clear);

        // EXCEPTIONS
        javalin.exception(BadRequestException.class, this::badRequestExceptionHandler);
        javalin.exception(UnauthorizedException.class, this::unauthorizedExceptionHandler);
        javalin.exception(AlreadyTakenException.class, this::alreadyTakenExceptionHandler);
        javalin.exception(Exception.class, this::exceptionHandler);

        // USER SERVICES
        UserService userService = new UserService(userDAO, authDAO);
        UserHandler userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        // GAME SERVICES
        GameService gameService = new GameService(authDAO, gameDAO);
        GameHandler gameHandler = new GameHandler(gameService);
        javalin.post("/game", gameHandler::createGame);
        javalin.get("/game", gameHandler::listGames);
        javalin.put("/game", gameHandler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void badRequestExceptionHandler(BadRequestException ex, Context ctx) {
        ctx.status(400);
        ctx.result(new Gson().toJson(Map.of("message", "Error: " + ex.getMessage())));
    }

    private void unauthorizedExceptionHandler(UnauthorizedException ex, Context ctx) {
        ctx.status(401);
        ctx.result(new Gson().toJson(Map.of("message", "Error: " + ex.getMessage())));
    }

    private void alreadyTakenExceptionHandler(AlreadyTakenException ex, Context ctx) {
        ctx.status(403);
        ctx.result(new Gson().toJson(Map.of("message", "Error: " + ex.getMessage())));
    }

    private void exceptionHandler(Exception ex, Context ctx) {
        ctx.status(500);
        ctx.result(new Gson().toJson(Map.of("message", "Error: " + ex.getMessage())));
    }
}
