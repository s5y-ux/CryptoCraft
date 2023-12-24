package net.ddns.vcccd;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CryptoMenuOption {
	
	@SuppressWarnings("unused")
	private ItemStack ItemRepresentation;
	
	public CryptoMenuOption(Map<?, ?> CoinMap) {
		//Creates an ItemStack as a Chest
        ItemStack value = new ItemStack(Material.GOLD_NUGGET);

        //Retrieves the Item Meta
        ItemMeta meta = value.getItemMeta();

        //Creates an Empty array list for the item lore
        ArrayList < String > Lore = new ArrayList < String > ();

        //This chunk of code is used in order to format the Lore of the item holding the crypto information
        Lore.add(ChatColor.BLUE + ">-------------------<");
        Lore.add(ChatColor.WHITE + "Symbol: " + ChatColor.AQUA + CoinMap.get("symbol").toString());
        Lore.add(ChatColor.WHITE + "Rank: " + ChatColor.AQUA + CoinMap.get("rank").toString());
        Lore.add(ChatColor.BLACK + ".");
        Lore.add(ChatColor.WHITE + "Price: " + ChatColor.GREEN + "$" + CoinMap.get("priceUsd").toString());
        Lore.add(ChatColor.WHITE + "Market Cap: " + ChatColor.DARK_GREEN + "$" + CoinMap.get("marketCapUsd").toString());
        if (Float.parseFloat(CoinMap.get("changePercent24Hr").toString()) < 0) {
            Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.RED + "%" + CoinMap.get("changePercent24Hr").toString());
        } else {
            Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.GREEN + "%" + CoinMap.get("changePercent24Hr").toString());
        }
        Lore.add(ChatColor.BLACK + ".");
        Lore.add(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + "$" + CoinMap.get("id").toString());
        Lore.add(ChatColor.BLUE + ">-------------------<");

        //Sets the lore in the meta
        meta.setLore(Lore);

        //Stores the name of the coin into a String object
        String name = CoinMap.get("name").toString();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&ki&f&l") + name + ChatColor.translateAlternateColorCodes('&', "&r&7&ki"));

        //Sets the meta and returns the ItemStack
        value.setItemMeta(meta);
        ItemRepresentation = value;
	}
	
	public ItemStack presentItem() {
		return(ItemRepresentation);
	}
}
