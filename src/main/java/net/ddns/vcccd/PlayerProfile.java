package net.ddns.vcccd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;

public class PlayerProfile {
	
	   // GUI element to represent the players profile
	   private ItemStack profileStack;

	    public PlayerProfile(Player player, Main main) {
	    	
	    	// The UUID and the string representation for the Database
	        UUID uniqueId = player.getUniqueId();
	        String idString = uniqueId.toString();
	        
	        // The connection to the Database
	        Connection conn = main.getPublicConnection();
	        
	        // Executors for query
	        Statement sqlExecutors;
	        
	        try {
	        	
	        	// Figure out if an instance of the players UUID exists in the Databse
	        	sqlExecutors = conn.createStatement();
		        String retrieveDataSQL = "SELECT Wallet FROM PlayerData WHERE UUID = '" + idString + "'";
		        ResultSet resultSet = sqlExecutors.executeQuery(retrieveDataSQL);
		        
		        // If not, return a player profile without lore 
		        if (!resultSet.next()) {
		        	
		        	// Add the player data into the Database TODO Optimize calls for player profile creation in Database
		            new PlayerData(main, player);
		            
		            //Create the ItemStack with the players profile information (or lack there of)
		            ItemStack playerProfileHead = new ItemStack(Material.PLAYER_HEAD);
	                SkullMeta playerProfileHeadSkin = (SkullMeta) playerProfileHead.getItemMeta();
	                playerProfileHeadSkin.setOwningPlayer(player);
	                playerProfileHeadSkin.setDisplayName(ChatColor.WHITE + player.getName() + "'s Profile");
	                playerProfileHead.setItemMeta(playerProfileHeadSkin);
	                
	                // Then set the private ItemStack attribute
	                this.profileStack = playerProfileHead;
	                
	            // On the condition the Player Data is in the Database
		        } else {
		        	
		        	//Take the JSON data from the Database
		        	String coinsData = resultSet.getString("Wallet");
		            Gson gson = new Gson();
		            JsonObject coinsObject = gson.fromJson(coinsData, JsonObject.class);
		            
		            // Then store the keys and values into an array
		            String[] keys = new String[coinsObject.size()];
		            String[] values = new String[coinsObject.size()];
		            
		            // Set the index to the amount of key, value pairs are in the Wallet
		            int index = 0;
		            for (Entry<String, JsonElement> entry : coinsObject.entrySet()) {
		                keys[index] = entry.getKey();
		                values[index] = entry.getValue().getAsString();
		                index++;
		            }
		            
		            // Now we create the GUI representation of the ItemStack
		            ItemStack playerProfileHead = new ItemStack(Material.PLAYER_HEAD);
	                SkullMeta playerProfileHeadSkin = (SkullMeta) playerProfileHead.getItemMeta();
	                playerProfileHeadSkin.setOwningPlayer(player);
	                
	                // Init the Lore Data
	                List<String> loreData = new ArrayList<>();

	                // Iterate through coin data keys and add them to the lore if value > 0
	                for(int i = 0; i < index; i++) {
	                	if (Integer.parseInt(values[i]) > 0) {
	                        loreData.add(ChatColor.GRAY + keys[i] + ": " + ChatColor.WHITE + values[i]);
	                    }
	                }

	                playerProfileHeadSkin.setLore(loreData);
	                playerProfileHeadSkin.setDisplayName(ChatColor.WHITE + player.getName() + "'s Profile");
	                playerProfileHead.setItemMeta(playerProfileHeadSkin);
	                
	                // Finally, set the private ItemStack Attribute to the GUI representation
	                this.profileStack = playerProfileHead;
		            
		        }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	    
	    // Public getter method for the ItemStack (GUI representation of the player profile)
	    public ItemStack returnStack() {
	        return this.profileStack;
	    }
	}

