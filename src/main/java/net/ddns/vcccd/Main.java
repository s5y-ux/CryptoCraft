package net.ddns.vcccd;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	//Used to access the console for the rest of the Main class
	private ConsoleCommandSender console = getServer().getConsoleSender();
	
	@Override
	public void onEnable() {
		console.sendMessage(ChatColor.GREEN + "Stock Market Plugin Enabled...");
		FileConfiguration config = this.getConfig();
		//File configuration code goes here:
		
		config.addDefault("API-key", "");
		
		this.saveDefaultConfig(); //Used to save configuration
		
		//Code for loading commands
		this.getCommand("test").setExecutor(new Test(this));
		
	}
	
	@Override
	public void onDisable() {
		
	}

}
