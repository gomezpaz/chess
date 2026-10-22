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

        // Register your endpoints and exception handlers here.

        // CLEAR
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::clear);

        // TODO: find a way to make exceptions cleaner
        javalin.exception(BadRequestException.class, this::badRequestExceptionHandler);
        javalin.exception(UnauthorizedException.class, this::unauthorizedExceptionHandler);
        javalin.exception(AlreadyTakenException.class, this::alreadyTakenExceptionHandler);
        javalin.exception(Exception.class, this::exceptionHandler);


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
