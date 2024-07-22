package net.ddns.vcccd;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

/**
 * Class for handling events related to the main GUI.
 */
public class MainGUIEvents implements Listener {
	
	/*
	 * Super straightforward code, 
	 * is it a coin? -> open coin menu...
	 * No? -> send respective message...
	 */
	 
	private boolean checkMaterial(ItemStack item, Material material) {
		return item.getType().equals(material);
	}

	@EventHandler
	public void onGlassPaneClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		try {
			if (event.getView().getTitle().equals("Cryptocurrency")) {
				ItemStack clickedItem = event.getCurrentItem();
				if (checkMaterial(clickedItem, Material.RED_STAINED_GLASS_PANE) ||
					checkMaterial(clickedItem, Material.BLACK_STAINED_GLASS_PANE)) {
					player.closeInventory();
				} else if (checkMaterial(clickedItem, Material.FEATHER)) {
					player.closeInventory();
					player.sendMessage(ChatColor.YELLOW + "https://www.blockchain.com/en/learning-portal");
				} else if (checkMaterial(clickedItem, Material.BOOK)) {
					player.closeInventory();
					player.sendMessage(ChatColor.GOLD + "https://www.blockchain.com/explorer/news");
				} else if (checkMaterial(clickedItem, Material.EMERALD)) {
					player.closeInventory();
					player.sendMessage(ChatColor.GREEN + "https://www.blockchain.com/explorer");
				} else {
					player.closeInventory();
					player.openInventory(new CoinGUI(clickedItem).getCoinInventory());
				}
			}
		} catch (Exception e) {
			assert true;
		}
	}
}
