package net.ddns.vcccd;

import net.ddns.vcccd.MainGUI;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Test implements CommandExecutor{
	
	private final Main main;
	
	public Test(Main main) {
		this.main = main;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			MainGUI GUI = new MainGUI(player, main);
			player.openInventory(GUI.getGUI());
		

			
		} else {
			sender.sendMessage(ChatColor.RED + "The Crypto Menu can only be accessed by a player...");
		}
		return(true);
	}
}