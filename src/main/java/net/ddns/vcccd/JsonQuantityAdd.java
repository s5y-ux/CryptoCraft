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
 * Class for adding a quantity of a certain coin to a player's wallet stored in a JSON format in a SQLite database.
 */
public class JsonQuantityAdd {
	
	private final Main main;
	private Connection conn;
	
	/*
	 * Note that the constructor method
	 * does the database actions. TODO streamline this
	 * into a class that handles all Database actions
	 * (This is what PlayerData was supposed to do)
	 * but I made it with YAML files and I'm still
	 * unsure how to do it with a data base using
	 * connections.
	 */
	
	public JsonQuantityAdd(Player player, String coin, int amount, Main main) {
		this.main = main;
		
		// UUID to be used as a varchar for the database
		UUID uniqueId = player.getUniqueId();
	    String idString = uniqueId.toString();
	    
	    // I will learn more about Database connections in the future
	    // TODO I just learned SQL, now it's time for me to learn
	    // better applications of it in code
	    this.conn = this.main.getPublicConnection();
	    Statement sqlExecutors;
	    
	    // TODO SteelPhinox told me that there are better
	    // ways to store UUIDs, should look into this.
	    // I think he may not be interested in further
	    // communications with me though :(
	    
	    try {
	        
	    	//Create statement and select the proper walled VIA UUID
	    	sqlExecutors = conn.createStatement();
	        String retrieveDataSQL = "SELECT Wallet FROM PlayerData WHERE UUID = '" + idString + "'";
	        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);
	        
	     // If no data found, insert empty JSON
	        if (!resultSet.next()) {
	            
	            String insertData = "INSERT INTO PlayerData (UUID, Wallet) VALUES ('" + idString + "', '{\"" + coin + "\": " + amount + "}')";
	            sqlExecutors.execute(insertData);
	            
	     // If data IS found, update existing data via SQL 
	        } else {
	        	
	        	//We get a string like {"key": value} 
	        	String coinsData = resultSet.getString("Wallet");
	        	
	        	//We instantiate a Gson object and assign a new JSON object with the data
	        	//in the coinsData string
	            Gson gson = new Gson();
	            JsonObject coinsObject = gson.fromJson(coinsData, JsonObject.class);
	            
	            //We create an Updated Amount
	            int UpdatedAmount;
	            try {
	            	
	            	//We check to see if the coin exists in the database of the player
	            	UpdatedAmount = coinsObject.get(coin).getAsInt() + amount;
	            	
	            } catch (Exception e) {
	            	
	            	//If it does not, we can assume a first time buy 
	            	UpdatedAmount = amount;
	            }
	            
	            //Finally, we set the value in the database to reflect the amount of coins the player has
	            String updateData = "UPDATE PlayerData SET Wallet = JSON_SET(Wallet, '$." + coin + "', " + UpdatedAmount + ") WHERE UUID = '" + idString + "'";
	            sqlExecutors.executeUpdate(updateData);
	            
	            //TODO I want to eventually make a class where you can obtain player information
	            //Via class methods i.e PlayerData data = new PlayerData(Player player);
	            //data.getCoinAmount("Coin");
	            

	        }
	        
	        // Close resources
	        resultSet.close();
	        sqlExecutors.close();
	        
	        // Inform player of successful purchase
	        player.sendMessage(main.prefix + ChatColor.GREEN + "Purchase successful!");
	    } catch (Exception e) {
	        this.main.getConsole().sendMessage("An error occurred during purchase.");
	        e.printStackTrace();
	        player.sendMessage(main.prefix + ChatColor.RED + "An error occurred during purchase. Please contact the Developer -> JsonQuantityAdd.java");
	    }
	}
}
