package net.ddns.vcccd;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SelectionPane {
	
	private ItemStack Pane;
	
	private String getPrice(ItemStack Coin) {
		ItemMeta CoinData = Coin.getItemMeta();
		ArrayList < String > Data = (ArrayList<String>) CoinData.getLore();
		String value = Data.get(4);
		String[] trueVal = value.split(" ");
		return(trueVal[1].substring(3));
	}
	
	private String PriceAmount(String price, long amount) {
		float value = Float.parseFloat(price) * (float) amount;
		return(Float.toString(value));
	}
	
	public SelectionPane(Material PaneType, ChatColor color, String name, long amount, ItemStack Coin) {
		ItemStack ReturnPane = new ItemStack(PaneType);
		ItemMeta PaneData = ReturnPane.getItemMeta();
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(ChatColor.YELLOW + "Amount: " + ChatColor.WHITE + Long.toString(amount));
		Lore.add(ChatColor.YELLOW + "Price: " + ChatColor.GREEN + "$" + PriceAmount(getPrice(Coin), amount));
		PaneData.setDisplayName(color + name);
		PaneData.setLore(Lore);
		ReturnPane.setItemMeta(PaneData);
		Pane = ReturnPane;
	}
	
	public ItemStack getItem() {
		return(Pane);
	}
}
