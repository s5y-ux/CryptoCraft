package net.ddns.vcccd;

import net.ddns.vcccd.MainGUI;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Test implements CommandExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			MainGUI GUI = new MainGUI();
			Player player = (Player) sender;
			player.openInventory(GUI.getGUI());
		

			
		}
		return(true);
	}
}