package net.ddns.vcccd;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SelectionPane {
	
	private ItemStack Pane;
	
	public SelectionPane(Material PaneType, ChatColor color, String name, long amount) {
		ItemStack ReturnPane = new ItemStack(PaneType);
		ItemMeta PaneData = ReturnPane.getItemMeta();
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(ChatColor.YELLOW + "Amount: " + ChatColor.WHITE + Long.toString(amount));
		PaneData.setDisplayName(color + name);
		PaneData.setLore(Lore);
		ReturnPane.setItemMeta(PaneData);
		Pane = ReturnPane;
	}
	
	public ItemStack getItem() {
		return(Pane);
	}
}
