package net.ddns.vcccd;

import org.bukkit.command.ConsoleCommandSender;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	//Used to access the console for the rest of the Main class
	private ConsoleCommandSender console = getServer().getConsoleSender();
	
	//Used to access In-Game Economy VIA Vault API
	private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	@Override
	public void onEnable() {
		
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
		
		console.sendMessage(ChatColor.GREEN + "Stock Market Plugin Enabled...");
		FileConfiguration config = this.getConfig();
		//File configuration code goes here:
		
		config.addDefault("RoundToDecimal", 2);
		
		this.saveDefaultConfig(); //Used to save configuration
		
		//Code for loading commands
		this.getCommand("test").setExecutor(new Test());
		
		//Code for registering for events
		getServer().getPluginManager().registerEvents(new MainGUIEvents(), this);
		getServer().getPluginManager().registerEvents(new CoinGUIEvents(), this);
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static Economy getEconomy() {
        return econ;
    }
    
    public static Permission getPermissions() {
        return perms;
    }
    
    public static Chat getChat() {
        return chat;
    }

}
