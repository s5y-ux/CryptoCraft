package net.ddns.vcccd;

import net.ddns.vcccd.MenuBarObject;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("unused")
public class CoinGUI {
	
	@SuppressWarnings("unused")
	private ArrayList <String> DataHandler;
	private ItemStack UsedCoin;
	
	public CoinGUI(ItemStack Coin) {
		ItemMeta CoinData = Coin.getItemMeta();
		ArrayList < String > Data = (ArrayList<String>) CoinData.getLore();
		DataHandler = Data;
		UsedCoin = Coin;
	}
	
	public Inventory getCoinInventory() {
		Inventory returnValue = Bukkit.createInventory(null, 27, "Buy | Sell");
		returnValue.setItem(4, UsedCoin);
		returnValue.setItem(13, new MenuBarObject(Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK, ".").presentItem());
		returnValue.setItem(22, new MenuBarObject(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Exit").presentItem());
		
		long BuySellAmount = 1;
		int[] StartingPoints = {4, 13, 22};
		for(int CurrentValue: StartingPoints) {
			for(int i = 1; i < 5; i++) {
				returnValue.setItem(CurrentValue-i, new SelectionPane(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.RED, "Sell", BuySellAmount).getItem());
				returnValue.setItem(CurrentValue+i, new SelectionPane(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN, "Buy", BuySellAmount).getItem());
				BuySellAmount = BuySellAmount * 10;
			}
		}
		
		return(returnValue);
	}
}
