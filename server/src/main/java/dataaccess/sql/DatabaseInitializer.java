package dataaccess.sql;
import dataaccess.*;

import dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInitializer {

    public DatabaseInitializer() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(256) NOT NULL PRIMARY KEY,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(256) NOT NULL PRIMARY KEY,
                username VARCHAR(256) NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(256) DEFAULT NULL,
                blackUsername VARCHAR(256) DEFAULT NULL,
                gameName VARCHAR(256) NOT NULL,
                game TEXT NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
