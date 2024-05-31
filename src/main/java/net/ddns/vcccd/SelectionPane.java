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
	
	/**
	 * Constructor for SelectionPane class.
	 * 
	 * @param PaneType The type of material for the pane
	 * @param color The color of the pane
	 * @param name The name of the pane
	 * @param amount The amount of the selected item
	 * @param Coin The selected coin item
	 */
	public SelectionPane(Material PaneType, ChatColor color, String name, long amount, ItemStack Coin) {
		ItemStack ReturnPane = new ItemStack(PaneType);
		ItemMeta PaneData = ReturnPane.getItemMeta();
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(Coin.getItemMeta().getLore().get(1).split(" ")[1]);
		Lore.add(ChatColor.YELLOW + "Amount: " + ChatColor.WHITE + Long.toString(amount));
		Lore.add(ChatColor.YELLOW + "Price: " + ChatColor.GREEN + "$" + PriceAmount(getPrice(Coin), amount));
		PaneData.setDisplayName(color + name);
		PaneData.setLore(Lore);
		ReturnPane.setItemMeta(PaneData);
		Pane = ReturnPane;
	}
	
	/**
	 * Retrieves the selection pane item.
	 * 
	 * @return The selection pane item
	 */
	public ItemStack getItem() {
		return Pane;
	}
	
	/**
	 * Retrieves the price of the selected coin.
	 * 
	 * @param Coin The selected coin item
	 * @return The price of the coin
	 */
	private String getPrice(ItemStack Coin) {
		ItemMeta CoinData = Coin.getItemMeta();
		ArrayList<String> Data = (ArrayList<String>) CoinData.getLore();
		String value = Data.get(4);
		String[] trueVal = value.split(" ");
		return trueVal[1].substring(3);
	}
	
	/**
	 * Calculates the total price based on the price of the coin and the selected amount.
	 * 
	 * @param price The price of the coin
	 * @param amount The selected amount
	 * @return The total price
	 */
	private String PriceAmount(String price, long amount) {
		float value = Float.parseFloat(price) * (float) amount;
		return Float.toString(value);
	}
}
