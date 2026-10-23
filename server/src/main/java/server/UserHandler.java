package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;

public class UserHandler {
    private final UserService userService;
    private final Gson serializer = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws DataAccessException {
        RegisterRequest body = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(body);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
    }

    public void login(Context ctx) throws DataAccessException {
        LoginRequest body = serializer.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(body);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
    }

    public void logout(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");
        userService.logout(new LogoutRequest(token));
        ctx.status(200);
        ctx.result("{}");
    }
}
