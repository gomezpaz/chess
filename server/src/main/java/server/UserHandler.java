package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;

public class UserHandler {
    private final UserService userService;
    private final Gson serializer = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws DataAccessException {
        RegisterRequest req = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(req);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
    }
}
