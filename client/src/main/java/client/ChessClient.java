package client;

import model.AuthData;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;

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
        return help();
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