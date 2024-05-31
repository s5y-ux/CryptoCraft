package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;

public class PlayerProfile {
	   private ItemStack profileStack;

	    public PlayerProfile(Player player, Main main) {
	        /*UUID uniqueId = player.getUniqueId();
	        String idString = uniqueId.toString();
	        Connection conn = main.getPublicConnection();
	        try {
	            // Prepare SQL statement to retrieve player's coin data
	            String retrieveDataSQL = "SELECT Wallet FROM PlayerData WHERE uuid = ?";
	            PreparedStatement selectStatement = conn.prepareStatement(retrieveDataSQL);
	            selectStatement.setString(1, idString);
	            ResultSet resultSet = selectStatement.executeQuery();

	            if (resultSet.next()) {
	                // Get JSON data from the database
	                String coinsData = resultSet.getString("Wallet");
	                Gson gson = new Gson();
	                JsonObject coinsObject = gson.fromJson(coinsData, JsonObject.class);

	                // Create player profile head ItemStack
	                ItemStack playerProfileHead = new ItemStack(Material.PLAYER_HEAD);
	                SkullMeta playerProfileHeadSkin = (SkullMeta) playerProfileHead.getItemMeta();
	                playerProfileHeadSkin.setOwningPlayer(player);
	                List<String> loreData = new ArrayList<>();

	                // Iterate through coin data keys and add them to the lore if value > 0
	                for (Entry<String, JsonElement> entry : coinsObject.entrySet()) {
	                    String key = entry.getKey();
	                    int value = entry.getValue().getAsInt();
	                    if (value > 0) {
	                        loreData.add(ChatColor.GRAY + key + ": " + ChatColor.WHITE + value);
	                    }
	                }
	                playerProfileHeadSkin.setLore(loreData);
	                playerProfileHeadSkin.setDisplayName(ChatColor.WHITE + player.getName() + "'s Profile");
	                playerProfileHead.setItemMeta(playerProfileHeadSkin);
	                this.profileStack = playerProfileHead;
	            } else {
	                // No data found for the player in the database
	                // Handle this scenario as needed
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle database errors
	        }*/
	    	this.profileStack = new ItemStack(Material.EMERALD);
	    }

	    public ItemStack returnStack() {
	        return this.profileStack;
	    }
	}

