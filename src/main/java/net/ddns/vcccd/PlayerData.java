package net.ddns.vcccd;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
	
	private String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&f[&eCryptoCraft&f] ");

    // Path where player data files are stored
    private String filePath = "plugins/CryptoCraft/PlayerData/";
    // YamlConfiguration instance to handle player data
    private YamlConfiguration RawData;
    // Player associated with this instance
    private Player player;

    // Constructor to initialize PlayerData object
    public PlayerData(Main main, Player player) {
        // Construct the path for the player's data file
        String UpdatedPath = filePath + player.getUniqueId().toString() + ".yml";
        File file = new File(UpdatedPath);
        this.player = player;
        // If the data file doesn't exist, create it and initialize RawData
        if (!file.exists()) {
            YamlConfiguration yamlFile = new YamlConfiguration();
            try {
                yamlFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.RawData = yamlFile;
        } else {
            // If the data file exists, load its contents into RawData
            this.RawData = YamlConfiguration.loadConfiguration(file);
        }
    }

    // Method to handle purchasing of coins
    public void Purchase(String coin, int amount) {
        String UpdatedPath = filePath + player.getUniqueId().toString() + ".yml";
        File file = new File(UpdatedPath);
        // If the coin section doesn't exist, create it and add the purchased amount
        if (!this.RawData.contains(coin)) {
            this.RawData.createSection(coin);
            this.RawData.set(coin, this.RawData.getInt(coin) + amount);
            try {
                this.RawData.save(file);
            } catch (IOException e) {
                // Print stack trace if an IO exception occurs during saving
                e.printStackTrace();
            }
        } else {
            // If the coin section exists, update its value with the purchased amount
            this.RawData.set(coin, amount);
            try {
                this.RawData.save(file);
            } catch (IOException e) {
                // Print stack trace if an IO exception occurs during saving
                e.printStackTrace();
            }
        }
    }

    // Method to handle selling of coins
    public boolean Sell(String coin, int amount) {
        String UpdatedPath = filePath + player.getUniqueId().toString() + ".yml";
        File file = new File(UpdatedPath);
        // Check if the player has the specified coin
        if (!this.RawData.contains(coin)) {
            this.player.sendMessage(pluginPrefix + ChatColor.RED + "You have none of that type of coin...");
            return false;
        } else if (this.RawData.getInt(coin) < amount) {
            // Check if the player has enough of the specified coin to sell
            this.player.sendMessage(pluginPrefix + ChatColor.RED + "You dont have " + Integer.toString(amount) + " " + coin + " you only have " + this.RawData.getInt(coin));
            return false;
        } else {
            // Subtract the sold amount from the player's coins
            this.RawData.set(coin, this.RawData.getInt(coin) - amount);
            try {
                // Save the updated player data
                this.RawData.save(file);
                return true;
            } catch (IOException e) {
                // Print stack trace if an IO exception occurs during saving
                e.printStackTrace();
                // Inform the player of a fatal error
                player.sendMessage(pluginPrefix + ChatColor.DARK_RED + "Fatal Error!");
                player.sendMessage(pluginPrefix + ChatColor.RED + "Problem with Sell Method in PlayerData. Please contact the developer...");
            }
        }
        return false;
    }
    
    public YamlConfiguration getRawData() {
    	return(this.RawData);
    }

}
