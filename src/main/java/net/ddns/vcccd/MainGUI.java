package net.ddns.vcccd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MainGUI{
	private final Main main;
	private Inventory Selection = Bukkit.createInventory(null, 54, "Cryptocurrency");
	
	public MainGUI(Main main) {
		this.main = main;
	}
	
	private ItemStack ControlItem(Material material, ChatColor chatcolor, String text) {
		ItemStack returnValue = new ItemStack(material);
		ItemMeta meta = returnValue.getItemMeta();
		meta.setDisplayName(chatcolor + text);
		returnValue.setItemMeta(meta);
		return(returnValue);
	}
	
	private ItemStack Option(Map<?, ?> reference) {
		ItemStack value = new ItemStack(Material.CHEST);
		ItemMeta meta = value.getItemMeta();
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(ChatColor.BLUE + ">-------------------<");
		Lore.add(ChatColor.WHITE + "Symbol: " + ChatColor.AQUA + reference.get("symbol").toString());
		Lore.add(ChatColor.WHITE + "Price: " + ChatColor.GREEN + "$" + reference.get("priceUsd").toString());
		if( Float.parseFloat(reference.get("changePercent24Hr").toString()) < 0) {
			Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.RED + "%" + reference.get("changePercent24Hr").toString());
		} else {
			Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.GREEN + "%" + reference.get("changePercent24Hr").toString());
		}
		Lore.add(ChatColor.BLUE + ">-------------------<");
		meta.setLore(Lore);
		String name = reference.get("name").toString();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&ki&f&l") + name + ChatColor.translateAlternateColorCodes('&', "&r&7&ki"));
		value.setItemMeta(meta);
		return(value);
	}
	
	public Inventory getGUI() {
		Stock stock = new Stock(main);
		List<?> test = (List<?>) stock.data().get("data");
		
		for(int i = 0; i < 45; i++) {
			Map<?, ?> reference = (Map<?, ?>)test.get(i);
			Selection.setItem(i, Option(reference));	
		}
		
		Selection.setItem(45, ControlItem(Material.EMERALD, ChatColor.GREEN, "Rich Market Data"));
		Selection.setItem(46, ControlItem(Material.BOOK, ChatColor.GOLD, "Cryptocurrency News"));
		Selection.setItem(47, ControlItem(Material.FEATHER, ChatColor.YELLOW, "Learn"));
		Selection.setItem(53, ControlItem(Material.RED_WOOL, ChatColor.RED, "Exit"));
		
		for(int x = 48; x<53; x++) {
			Selection.setItem(x, ControlItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK, "."));
		}
		return(Selection);
	}
	
}
