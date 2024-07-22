package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class for removing a quantity of a certain coin from a player's wallet stored in a JSON format in a SQLite database.
 */
public class JsonQuantityRemove {
	private final Main main;
	private Connection conn;
	private boolean Trigger;
	
	/*
	 * Very similar to quantity add, but now
	 * it removes coins. A little bit more in game
	 * communication and logic though.
	 */
	
	public JsonQuantityRemove(Player player, String coin, int saleAmount, Main main) {
		this.main = main;
		
		// Gets the UUID as varchar and the connection
		UUID uniqueId = player.getUniqueId();
	    String idString = uniqueId.toString();
	    this.conn = this.main.getPublicConnection();
	    Statement sqlExecutors;
	    
	    
	    try {
	    	
	    	// Get the JSON data from the respective player UUID
	    	sqlExecutors = conn.createStatement();
	    	String retrieveDataSQL = "SELECT Wallet FROM PlayerData WHERE UUID = '" + idString + "'";
	        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);
	        
	        // The resultSet.next() will always have info unless
	    	// changed in the Events class
	    	if (!resultSet.next()) {
	            player.sendMessage(main.prefix + ChatColor.RED + "You haven't purchased any coins.");
	        } 
	    	
	    	// If data found, update existing data
	    	else {
	            
	    		// Gets the wallet column from the Database and handles it as JsonObject
	        	String coinsData = resultSet.getString("Wallet");
	            Gson gson = new Gson();
	            JsonObject coinsObject = gson.fromJson(coinsData, JsonObject.class);
	            
	            //If the waller doesn't have the coin code say they don't have the coin
	            if (!coinsObject.has(coin)) {
	                player.sendMessage(main.prefix + ChatColor.RED + "You have none of that type of coin...");
	                this.Trigger = false;
	                
	            // If the object does have the coin code, but not enough for the sale relay the information
	            } else if (coinsObject.get(coin).getAsInt() < saleAmount) {
	                player.sendMessage(main.prefix + ChatColor.RED + "You don't have " + saleAmount + " " + coin + ". You only have " + coinsObject.get(coin).getAsInt());
	                this.Trigger = false;
	                
	                // TODO if the coin code is in the wallet, but they have zero, it will say they have zero
	                // so it should probably be included in the logic of the (!coin) by (!coin or coinsObject.get(coin).getAsInt() == 0) 
	                
	            } else {
	                // Subtract the sold amount from the player's coins
	                int currentAmount = coinsObject.get(coin).getAsInt();
	                
	                // Prepare SQL statement to update player's coin data
	                String updateData = "UPDATE PlayerData SET Wallet = JSON_SET(Wallet, '$." + coin + "', " + (currentAmount - saleAmount) + ") WHERE UUID = '" + idString + "'";
		            sqlExecutors.executeUpdate(updateData);
	                this.Trigger = true;
	            }
	        }
	    	resultSet.close();
	        sqlExecutors.close();
	    } catch (Exception e){
	    	this.main.getConsole().sendMessage("Error occurred");
	    }
	}
	
	/**
	 * Checks if the removal of coins was successful.
	 * 
	 * @return True if the removal was successful, otherwise false
	 * this was determined by the logic of the constructor.
	 */
	public boolean isSuccessful() {
		return this.Trigger;
	}
}
