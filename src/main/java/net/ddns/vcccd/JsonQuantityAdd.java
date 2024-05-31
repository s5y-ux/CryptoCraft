package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class for adding a quantity of a certain coin to a player's wallet stored in a JSON format in a SQLite database.
 */
public class JsonQuantityAdd {
	
	private final Main main;
	private Connection conn;
	private String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&f[&eCryptoCraft&f] ");
	
	/**
	 * Constructor for JsonQuantityAdd class.
	 * 
	 * @param player The player to add the coins to
	 * @param coin The name of the coin
	 * @param amount The amount of the coin to add
	 * @param main The main plugin instance
	 */
	public JsonQuantityAdd(Player player, String coin, int amount, Main main) {
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
	            // If no data found, insert new data
	            String insertData = "INSERT INTO PlayerData (UUID, Wallet) VALUES ('" + idString + "', '{\"" + coin + "\": " + amount + "}')";
	            sqlExecutors.execute(insertData);
	        } else {
	            // If data found, update existing data
	            String updateData = "UPDATE PlayerData SET Wallet = JSON_SET(Wallet, '$." + coin + "', " + amount + ") WHERE UUID = '" + idString + "'";
	            sqlExecutors.executeUpdate(updateData);
	        }
	        // Close resources
	        resultSet.close();
	        sqlExecutors.close();
	        // Inform player of successful purchase
	        player.sendMessage(pluginPrefix + ChatColor.GREEN + "Purchase successful!");
	    } catch (Exception e) {
	        this.main.getConsole().sendMessage("An error occurred during purchase.");
	        e.printStackTrace();
	        player.sendMessage(pluginPrefix + ChatColor.RED + "An error occurred during purchase. Please contact the server administrator.");
	    }
	}
}
