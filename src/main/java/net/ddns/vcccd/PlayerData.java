package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.entity.Player;


/**
 * Class for managing player data.
 */
public class PlayerData {
	
	private Connection conn;
	private final Main main;
	
	/**
	 * Constructor for PlayerData class.
	 * 
	 * @param main The main plugin instance
	 * @param player The player
	 */
	public PlayerData(Main main, Player player) {
		this.main = main;
	    UUID uniqueId = player.getUniqueId();
	    String idString = uniqueId.toString();
	    this.conn = this.main.getPublicConnection();
	    
	    try {
	        Statement sqlExecutors = this.conn.createStatement();
	        String retrieveDataSQL = "SELECT * FROM PlayerData WHERE UUID = '" + idString + "'";
	        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);

	        if (!resultSet.next()) {
	            // If no data found, insert new data
	            String insertData = "INSERT INTO PlayerData (UUID, Wallet) VALUES ('" + idString + "', '{}')";
	            sqlExecutors.execute(insertData);
	        }

	        // Close resources
	        resultSet.close();
	        sqlExecutors.close();
	    } catch (SQLException e) {
	        // Handle SQL exception
	        e.printStackTrace();
	        this.main.getConsole().sendMessage("An error occurred during PlayerData initialization.");
	    }
	}
}
