package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.entity.Player;


/**
 * Class for Instantiating Players existence in the database
 */
public class PlayerData {
	
	/*
	 * Takes the main class and the player,
	 * finds the UUID and then inserts it into
	 * the Database
	 */
	public PlayerData(Main main, Player player) {
		
		//gets the UUID of the player
	    UUID uniqueId = player.getUniqueId();
	    String idString = uniqueId.toString();
	    
	    //Gets the connection
	    Connection conn = main.getPublicConnection();
	    
	    try {
	    	
	    	//Selects all the data with the UUID (Just trying to find an instance of that UUID)
	        Statement sqlExecutors = conn.createStatement();
	        String retrieveDataSQL = "SELECT * FROM PlayerData WHERE UUID = '" + idString + "'";
	        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);

	        // If no data found, insert blank data
	        if (!resultSet.next()) {
	            String insertData = "INSERT INTO PlayerData (UUID, Wallet) VALUES ('" + idString + "', '{}')";
	            sqlExecutors.execute(insertData);
	        }

	        // Close resources
	        resultSet.close();
	        sqlExecutors.close();
	        
	    } catch (SQLException e) {
	        // Handle SQL exception
	        e.printStackTrace();
	        main.getConsole().sendMessage(main.prefix + "An error occurred during PlayerData initialization.");
	    }
	}
}
