
package client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import chess.ChessGame;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson serializer = new Gson();
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path));
        if (authToken != null) { request.header("authorization", authToken); }
        if (body != null) { request.header("Content-Type", "application/json"); }
        request.method(method, makeRequestBody(body));

        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object body) {
        if (body == null) { return HttpRequest.BodyPublishers.noBody(); }
        return HttpRequest.BodyPublishers.ofString(serializer.toJson(body));
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ResponseException("Unable to connect to server");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseException("Request interrupted");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> resultClass) {
        if (response.statusCode() >= 500) { throw new ResponseException("Server error"); }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            String message = "Request failed";
            try {
                ErrorResponse error = serializer.fromJson(response.body(), ErrorResponse.class);
                if (error != null && error.message() != null) { message = error.message(); }
            } catch (JsonSyntaxException ignored) {}
            throw new ResponseException(message);
        }
        if (resultClass == null) { return null; }
        return serializer.fromJson(response.body(), resultClass);
    }


    private <T> T makeRequest(String method, String path, Object body, String authToken, Class<T> resultClass) {
        HttpRequest request = buildRequest(method, path, body, authToken);
        HttpResponse<String> response = sendRequest(request);


        return handleResponse(response, resultClass);
    }


    public AuthData register(String username, String password, String email) {
        return makeRequest("POST", "/user", new UserData(username, password, email), null, AuthData.class);
    }

    public AuthData login(String username, String password) {
        return makeRequest("POST", "/session", new LoginRequest(username, password), null, AuthData.class);
    }

    public void logout(String authToken) {
        makeRequest("DELETE", "/session", null, authToken, null);
    }

    public void clear() {
        makeRequest("DELETE", "/db", null, null, null);
    }


    public int createGame(String authToken, String gameName) {
        CreateGameResponse result = makeRequest("POST", "/game", new CreateGameRequest(gameName), authToken, CreateGameResponse.class);
        return result.gameID();
    }

    public Collection<GameData> listGames(String authToken) {
        ListGamesResponse result = makeRequest("GET", "/game", null, authToken, ListGamesResponse.class);
        return result.games();
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) {
        makeRequest("PUT", "/game", new JoinGameRequest(color, gameID), authToken, null);
    }


    private record CreateGameRequest(String gameName) {
    }

    private record CreateGameResponse(int gameID) {
    }

    private record ListGamesResponse(Collection<GameData> games) {
    }

    private record LoginRequest(String username, String password) {
    }

    private record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
    }


    private record ErrorResponse(String message) {
    }
}
