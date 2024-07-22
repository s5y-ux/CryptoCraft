package net.ddns.vcccd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class for handling command that access the menu.
 * 
 * This is the father class of the project and is used for
 * the first layer of GUI Access.
 */
public class MenuAccessor implements CommandExecutor{
	
	private final Main main;
	public MenuAccessor(Main main) {
		this.main = main;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// If the person sending the command is a player,
		// Open up the menu
		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			MainGUI GUI = new MainGUI(player, main);
			player.openInventory(GUI.getGUI());
			
		// Otherwise, deny access.
		} else {
			sender.sendMessage(ChatColor.RED + "The Crypto Menu can only be accessed by a player...");
		}
		return true;
	}
}
