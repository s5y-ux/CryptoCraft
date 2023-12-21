package net.ddns.vcccd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Test implements CommandExecutor{
	
	private final Main main;

	public Test(Main main) {
		this.main = main;
	}
	
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			MainGUI GUI = new MainGUI(main);
			Player player = (Player) sender;
			player.openInventory(GUI.getGUI());
			
		}
		return(true);
	}
}
