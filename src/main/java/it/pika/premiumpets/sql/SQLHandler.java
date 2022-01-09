package it.pika.premiumpets.sql;

import it.pika.premiumpets.PremiumPets;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLHandler {

    private File databaseFile;

    public Connection connection;

    public SQLHandler(){
        databaseFile = new File(PremiumPets.getInstance().getDataFolder(), "storage.db");
        if(!databaseFile.exists()){
            try {
                databaseFile.createNewFile();
                Bukkit.getLogger().info("[PremiumPets] Storage file created.");
            } catch (IOException e) {
                Bukkit.getLogger().warning("[PremiumPets] " + e.getMessage());
            }
        }
        connect();
        setupDatabase();
    }

    private void connect(){
        try {
            String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            Bukkit.getLogger().info("[PremiumPets] Connection to SQLite has been established.");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[PremiumPets] " + e.getMessage());
        }
    }

    public void closeConnection(){
        try {
            if (connection != null){
                connection.close();
                Bukkit.getLogger().info("[PremiumPets] Connection to SQLite has been closed.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[PremiumPets] " + e.getMessage());
        }
    }

    @SneakyThrows
    private void setupDatabase(){
        try {
            String QUERY = "CREATE TABLE IF NOT EXISTS `pets` (" +
                    "  `petid` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  `ownerUUID` TEXT NOT NULL," +
                    "  `ownerName` TEXT NOT NULL," +
                    "  `petName` VARCHAR(45) NOT NULL)";

            PreparedStatement stmt = connection.prepareStatement(QUERY);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[PremiumPets] " + e.getMessage());
        }
    }

    @SneakyThrows
    public int getRows(String tableName){
        String QUERY = "SELECT COUNT(*) AS rowCount FROM " + tableName;
        PreparedStatement stmt = connection.prepareStatement(QUERY);
        ResultSet rs = stmt.executeQuery();
        int count = 0;
        if(rs.next()){
            count = rs.getInt("rowCount");
        }
        stmt.close();
        return count;
    }

}
