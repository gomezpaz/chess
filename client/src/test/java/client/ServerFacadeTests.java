package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.Collection;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }



    @Test
    @DisplayName("Register Successful")
    public void registerSuccessful() {
        AuthData auth = facade.register("u","p","e");
        Assertions.assertEquals("u", auth.username());
        Assertions.assertTrue(auth.authToken().length() > 10);
    }

    @Test
    @DisplayName("Register Duplicate")
    public void registerDuplicate() {
        facade.register("u","p","e");
        Assertions.assertThrows(ResponseException.class, () -> facade.register("u","p","e"));
    }

    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() {
        facade.register("u","p","e");
        AuthData auth = facade.login("u","p");
        Assertions.assertNotNull(auth.authToken());
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() {
        facade.register("u","p","e");
        Assertions.assertThrows(ResponseException.class, () -> facade.login("u","a"));
    }

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccessful() {
        AuthData auth = facade.register("u","p","e");
        Assertions.assertDoesNotThrow(() -> facade.logout(auth.authToken()));
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Logout Invalid Token")
    public void logoutInvalidToken() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("a"));
    }

    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessful() {
        facade.register("u","p","e");
        facade.clear();
        Assertions.assertThrows(ResponseException.class, () -> facade.login("u","p"));
    }


    @Test
    @DisplayName("Create Game Successful")
    public void createGameSuccessful() {
        AuthData auth = facade.register("u","p","e");
        int id = facade.createGame(auth.authToken(), "a");
        Assertions.assertTrue(id > 0);
    }

    @Test
    @DisplayName("Create Game Invalid Token")
    public void createGameInvalidToken() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("a","b"));
    }

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() {
        AuthData auth = facade.register("u","p","e");
        facade.createGame(auth.authToken(), "a");
        facade.createGame(auth.authToken(), "b");
        Collection<GameData> games = facade.listGames(auth.authToken());

        Assertions.assertEquals(2, games.size());

        boolean hasA = false;
        boolean hasB = false;

        for (GameData game : games) {
            if (game.gameName().equals("a")) { hasA = true; }
            if (game.gameName().equals("b")) { hasB = true; }
        }

        Assertions.assertTrue(hasA);
        Assertions.assertTrue(hasB);
    }


    @Test
    @DisplayName("List Games Invalid Token")
    public void listGamesInvalidToken() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames("a"));
    }

}
