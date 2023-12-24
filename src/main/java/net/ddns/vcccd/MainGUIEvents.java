package net.ddns.vcccd;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class MainGUIEvents implements Listener {
	
	private boolean checkMaterial(ItemStack item, Material material) {
		return(item.getType().equals(material));
	}
	
	@EventHandler
	public void onGlassPaneClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getView().getTitle().equals("Cryptocurrency")) {
			ItemStack ClickedItem = event.getCurrentItem();
			if(checkMaterial(ClickedItem, Material.RED_STAINED_GLASS_PANE)) {
				player.closeInventory();
			} else if(checkMaterial(ClickedItem, Material.BLACK_STAINED_GLASS_PANE)) {
				player.closeInventory();
			} else if(checkMaterial(ClickedItem, Material.FEATHER)) {
				player.closeInventory();
				player.sendMessage(ChatColor.YELLOW + "https://www.blockchain.com/en/learning-portal");
			} else if(checkMaterial(ClickedItem, Material.BOOK)) {
				player.closeInventory();
				player.sendMessage(ChatColor.GOLD + "https://www.blockchain.com/explorer/news");
			} else if(checkMaterial(ClickedItem, Material.EMERALD)) {
				player.closeInventory();
				player.sendMessage(ChatColor.GREEN + "https://www.blockchain.com/explorer");
			} else {
				player.closeInventory();
				player.openInventory(new CoinGUI(ClickedItem).getCoinInventory());
			}
		} 
	}

}
