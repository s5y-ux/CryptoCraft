package net.ddns.vcccd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class PlayerProfile {
	
	private ItemStack profileStack;
	
	public PlayerProfile(Player player, Main main) {
		PlayerData playerCoinInfo = new PlayerData(main, player);
		YamlConfiguration Data = playerCoinInfo.getRawData();
		ItemStack playerProfileHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta playerProfileHeadSkin = (SkullMeta) playerProfileHead.getItemMeta();
		playerProfileHeadSkin.setOwningPlayer(player);
		Set<String> dataKeys = Data.getKeys(false);
		List<String> loreData = new ArrayList<String>();
		for (String key : dataKeys) {
			if(Data.getInt(key) > 0) {
			loreData.add(ChatColor.GRAY + key + ": " + ChatColor.WHITE + Data.getInt(key));		
			}
		}
		playerProfileHeadSkin.setLore(loreData);
		playerProfileHeadSkin.setDisplayName(ChatColor.WHITE + player.getName() + "'s " + "Profile");
		playerProfileHead.setItemMeta(playerProfileHeadSkin);
		this.profileStack = playerProfileHead;
	}
	
	public ItemStack returnStack() {
		return(this.profileStack);
	}
	
}
