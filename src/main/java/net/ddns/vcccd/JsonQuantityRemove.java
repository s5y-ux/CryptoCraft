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
	private String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&f[&eCryptoCraft&f] ");
	private boolean Trigger;
	
	/**
	 * Constructor for JsonQuantityRemove class.
	 * 
	 * @param player The player to remove the coins from
	 * @param coin The name of the coin
	 * @param saleAmount The amount of the coin to remove
	 * @param main The main plugin instance
	 */
	public JsonQuantityRemove(Player player, String coin, int saleAmount, Main main) {
		this.main = main;
		UUID uniqueId = player.getUniqueId();
	    String idString = uniqueId.toString();
	    this.conn = this.main.getPublicConnection();
	    Statement sqlExecutors;
	    try {
	    	sqlExecutors = this.conn.createStatement();
	    	String retrieveDataSQL = "SELECT Wallet FROM PlayerData WHERE UUID = '" + idString + "'";
	        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);
	    	if (!resultSet.next()) {
	            player.sendMessage(pluginPrefix + ChatColor.RED + "You haven't purchased any coins.");
	        } else {
	            // If data found, update existing data
	        	String coinsData = resultSet.getString("Wallet");
	            Gson gson = new Gson();
	            JsonObject coinsObject = gson.fromJson(coinsData, JsonObject.class);
	            if (!coinsObject.has(coin)) {
	                player.sendMessage(pluginPrefix + ChatColor.RED + "You have none of that type of coin...");
	                this.Trigger = false;
	            } else if (coinsObject.get(coin).getAsInt() < saleAmount) {
	                // Check if the player has enough of the specified coin to sell
	                player.sendMessage(pluginPrefix + ChatColor.RED + "You don't have " + saleAmount + " " + coin + ". You only have " + coinsObject.get(coin).getAsInt());
	                this.Trigger = false;
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
	 */
	public boolean isSuccessful() {
		return this.Trigger;
	}
}
