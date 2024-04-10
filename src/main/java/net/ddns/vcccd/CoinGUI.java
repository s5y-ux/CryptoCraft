package net.ddns.vcccd;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

//This is the GUI that is opened when a player clicks on a Coin in the main menu
public class CoinGUI {
	
	//Private ItemStack used for coin Data. This needs to be passed into the purchase pannels.
	private ItemStack UsedCoin;
	
	//Constructor. We pass the coin ItemStack from the Main GUI to pull our data.
	//It is designed this way to probe the API as little as possible.
	public CoinGUI(ItemStack Coin) {
		UsedCoin = Coin;
	}
	
	//This is the public method used to construct and access the GUI
	public Inventory getCoinInventory() {

		//Creates the actual inventory object.
		Inventory returnValue = Bukkit.createInventory(null, 27, "Buy | Sell");

		//These items are all in the middle of the inventory.
		//They are our selected coin and GUI use items.
		returnValue.setItem(4, UsedCoin);
		returnValue.setItem(13, new MenuBarObject(Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK, ".").presentItem());
		returnValue.setItem(22, new MenuBarObject(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Exit").presentItem());
		
		//Pretty proud of this algorithm.
		//This is used to store the options for how many coins we should buy.

		//Acumulator for amount of coins to buy
		long BuySellAmount = 1;

		//These are the middle parts of the various rows of the GUI
		int[] StartingPoints = {4, 13, 22};

		//Embedded for loop to add buy and sell panes on their respective slots
		for(int CurrentValue: StartingPoints) {

			//Starts from the middle pane and moves i slots to the left and right
			for(int i = 1; i < 5; i++) {
				returnValue.setItem(CurrentValue-i, new SelectionPane(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.RED, "Sell", BuySellAmount, UsedCoin).getItem());
				returnValue.setItem(CurrentValue+i, new SelectionPane(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN, "Buy", BuySellAmount, UsedCoin).getItem());
				
				//Uses base 10 numbering for the amounts
				BuySellAmount = BuySellAmount * 10;
			}
		}
		
		//Returns the constructed GUI
		return(returnValue);
	}
}
