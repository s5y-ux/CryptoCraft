package net.ddns.vcccd;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MenuBarObject {
	
	@SuppressWarnings("unused")
	private ItemStack ItemRepresentation;
	
	public MenuBarObject(Material material, ChatColor chatcolor, String text) {
		//Creates a ItemStack of the specified material
        ItemStack returnValue = new ItemStack(material);

        //Retrieves the item meta and stores it
        ItemMeta meta = returnValue.getItemMeta();

        //Changes the meta to include the custom name
        meta.setDisplayName(chatcolor + text);

        //Sets the Item Meta
        returnValue.setItemMeta(meta);

        //Then returns the custom ItemStack
        ItemRepresentation = returnValue;
	}
	
	public ItemStack presentItem() {
		return(ItemRepresentation);
	}
}
