package server;

import service.ClearService;
import io.javalin.http.Context;
import dataaccess.*;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.result("{}");
    }
}
