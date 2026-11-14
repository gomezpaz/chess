package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private List<GameData> games = new ArrayList<>();

    public ChessClient(int port) {
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println(help());
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>> ");
            if (!scanner.hasNextLine()) { break; }
            String line = scanner.nextLine();
            try {
                result = eval(line);
                if (!result.equals("quit")) { System.out.println(result); }
            } catch (ResponseException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong");
            }
        }
    }

    public String eval(String input) {
        String[] tokens = input.trim().split("\\s+");
        String cmd = tokens[0].toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (cmd.isBlank()) { return help(); }
        if (state == State.SIGNEDOUT) {
            return switch (cmd) {
                case "help" -> params.length == 0 ? help() : "Expected: help";
                case "quit" -> params.length == 0 ? "quit" : "Expected: quit";
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        }
        return switch (cmd) {
            case "help" -> params.length == 0 ? help() : "Expected: help";
            case "logout" -> logout(params);
            case "create" -> createGame(params);
            case "list" -> listGames(params);
            case "play" -> playGame(params);
            case "observe" -> observeGame(params);
            default -> help();
        };
    }

    public String register(String... params) {
        if (params.length != 3) { return "Expected: register <username> <password> <email>"; }
        AuthData auth = server.register(params[0], params[1], params[2]);
        authToken = auth.authToken();
        state = State.SIGNEDIN;
        return "Registered as " + auth.username();
    }

    public String login(String... params) {
        if (params.length != 2) { return "Expected: login <username> <password>"; }
        AuthData auth = server.login(params[0], params[1]);
        authToken = auth.authToken();
        state = State.SIGNEDIN;
        return "Logged in as " + auth.username();
    }

    public String logout(String... params) {
        if (params.length != 0) { return "Expected: logout"; }
        server.logout(authToken);
        authToken = null;
        state = State.SIGNEDOUT;
        games.clear();
        return "Logged out";
    }

    public String createGame(String... params) {
        if (params.length == 0) { return "Expected: create <name>"; }
        String name = String.join(" ", params);
        server.createGame(authToken, name);
        games.clear();
        return "Created game " + name;
    }

    public String listGames(String... params) {
        if (params.length != 0) { return "Expected: list"; }
        games = new ArrayList<>(server.listGames(authToken));
        if (games.isEmpty()) { return "No games available"; }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            String white = game.whiteUsername() == null ? "open" : game.whiteUsername();
            String black = game.blackUsername() == null ? "open" : game.blackUsername();
            result.append(i + 1).append(". ").append(game.gameName()).append(" | White: ").append(white).append(" | Black: ").append(black).append("\n");
        }
        return result.toString();
    }

    private GameData getGame(String number) {
        int i;
        try {
            i = Integer.parseInt(number) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid game number");
        }
        if (games.isEmpty()) { games = new ArrayList<>(server.listGames(authToken)); }
        if (i < 0 || i >= games.size()) { throw new IllegalArgumentException("Game not found"); }
        return games.get(i);
    }

    public String playGame(String... params) {
        if (params.length != 2) { return "Expected: play <number> <WHITE|BLACK>"; }
        if (!params[1].equalsIgnoreCase("WHITE") && !params[1].equalsIgnoreCase("BLACK")) { return "Color must be WHITE or BLACK"; }
        ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
        GameData game = getGame(params[0]);
        server.joinGame(authToken, color, game.gameID());
        return "Joined game " + game.gameName() + " as " + color;
    }

    public String observeGame(String... params) {
        if (params.length != 1) { return "Expected: observe <number>"; }
        GameData game = getGame(params[0]);
        return "Observing game " + game.gameName();
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Available commands:
                    help
                    register <username> <password> <email>
                    login <username> <password>
                    quit
                    """;
        }
        return """
                Available commands:
                help
                logout
                create <name>
                list
                play <number> <WHITE|BLACK>
                observe <number>
                """;
    }
}