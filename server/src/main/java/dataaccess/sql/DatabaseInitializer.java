package dataaccess.sql;

public class DatabaseInitializer {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(256) NOT NULL PRIMARY KEY,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(256) NOT NULL PRIMARY KEY,
                username VARCHAR(256) NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(256) DEFAULT NULL,
                blackUsername VARCHAR(256) DEFAULT NULL,
                gameName VARCHAR(256) NOT NULL,
                game TEXT NOT NULL
            )
            """
    };
}
