package net.ddns.vcccd;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Class for creating selection panes with specific properties.
 */
public class SelectionPane {
	
	private ItemStack Pane;
	
	// This basically creates the item representation of the buy or sell panes in the GUI
	public SelectionPane(Material PaneType, ChatColor color, String name, long amount, ItemStack Coin) {
		
		// Creates the Item of the pane type
		ItemStack ReturnPane = new ItemStack(PaneType);
		
		// Gets the item meta and updates it with the price and amount
		ItemMeta PaneData = ReturnPane.getItemMeta();
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(Coin.getItemMeta().getLore().get(1).split(" ")[1]);
		Lore.add(ChatColor.YELLOW + "Amount: " + ChatColor.WHITE + Long.toString(amount));
		Lore.add(ChatColor.YELLOW + "Price: " + ChatColor.GREEN + "$" + PriceAmount(getPrice(Coin), amount));
		
		// Sets the name to buy or sell and updates the Lore
		PaneData.setDisplayName(color + name);
		PaneData.setLore(Lore);
		
		// Sets all the info to the item
		ReturnPane.setItemMeta(PaneData);
		
		// Finally, it sets the private ItemStack attribute to the pane.
		Pane = ReturnPane;
	}
	
	// Getter method for the private ItemStack
	public ItemStack getItem() {
		return Pane;
	}
	
	// TODO update this to work as long, but for now returns a string representation of the price
	private String getPrice(ItemStack Coin) {
		ItemMeta CoinData = Coin.getItemMeta();
		ArrayList<String> Data = (ArrayList<String>) CoinData.getLore();
		String value = Data.get(4);
		String[] trueVal = value.split(" ");
		return trueVal[1].substring(3);
	}
	
	// Calculate the transaction price by the coin quantity and multiply it by the price of the coin
	private String PriceAmount(String price, long amount) {
		float value = Float.parseFloat(price) * (float) amount;
		return Float.toString(value);
	}
}
