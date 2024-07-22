package net.ddns.vcccd;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.command.ConsoleCommandSender;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

/**
 * The main class of the CryptoCraft plugin.
 */
public class Main extends JavaPlugin {
	
	// Used to access the console for the rest of the Main class
	private ConsoleCommandSender console = getServer().getConsoleSender();
	
	// Prefix for plugin messages
	public final String prefix = ChatColor.translateAlternateColorCodes('&', "&f[&eCryptoCraft&f] - ");
	
	// Connection to the SQLite database
	private Connection publicConnection;
	
	// Vault services for economy, permissions, and chat
	private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    
    /**
     * Retrieves the console sender.
     * 
     * @return The console command sender
     */
    public ConsoleCommandSender getConsole() {
    	return this.console;
    }
	
    /**
     * Sets up the economy service via Vault.
     * 
     * @return True if the economy service is successfully set up, otherwise false
     */
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
    
    /**
     * Sets up the chat service via Vault.
     * 
     * @return True if the chat service is successfully set up, otherwise false
     */
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
            return chat != null;
        } else {
            getLogger().warning("Chat service provider is not available.");
            return false;
        }
    }
    
    /**
     * Sets up the permissions service via Vault.
     * 
     * @return True if the permissions service is successfully set up, otherwise false
     */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	@Override
	public void onEnable() {
		// Check if Vault is available and set up economy, permissions, and chat
		if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
		
		// Load configuration
		FileConfiguration config = this.getConfig();
		config.addDefault("RoundToDecimal", 2);
		this.saveDefaultConfig();
		
		// Set up commands and event listeners
		this.getCommand("crypto").setExecutor(new MenuAccessor(this));
		getServer().getPluginManager().registerEvents(new MainGUIEvents(), this);
		getServer().getPluginManager().registerEvents(new CoinGUIEvents(this), this);
		
		// Check and create database file if not exists
		File rootDirectory = new File("plugins/CryptoCraft/PlayerData.db");
		if(!rootDirectory.exists()) {
			try {
				if(rootDirectory.createNewFile()) {
					console.sendMessage(prefix + "File path created, no errors!");
				} else {
					console.sendMessage(prefix + ChatColor.RED + "An error occured when creating directory, message the developer.");
				}
			} catch (IOException e) {
				console.sendMessage(prefix + ChatColor.RED + "FATAL ERROR! Can't even check for database file existence! Jesus...");
			}
		}
		
		// Connect to the database
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + "plugins/CryptoCraft/PlayerData.db");
			this.publicConnection = conn;
			console.sendMessage(prefix + ChatColor.YELLOW + "Connected to Database successfully!");
		} catch (SQLException e) {
			console.sendMessage(prefix + ChatColor.RED + "Database machine broke... (onEnable) nag the developer!");
		}
		
		// Create table if not exists
		Statement statement;
		try {
			statement = this.publicConnection.createStatement();
			String createTableSQL = "CREATE TABLE IF NOT EXISTS PlayerData (\n"
                    + "UUID VARCHAR(50),\n"
                    + "Wallet JSON"
                    + ");";
	        statement.execute(createTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		// Close database connection on disable
		try {
			this.publicConnection.close();
		} catch (SQLException e) {
			console.sendMessage(prefix + ChatColor.RED + "Problem disabling database in onDisable");
		}
	}
	
	/**
     * Retrieves the economy service.
     * 
     * @return The economy service
     */
    public static Economy getEconomy() {
        return econ;
    }
    
    /**
     * Retrieves the permissions service.
     * 
     * @return The permissions service
     */
    public static Permission getPermissions() {
        return perms;
    }
    
    /**
     * Retrieves the chat service.
     * 
     * @return The chat service
     */
    public static Chat getChat() {
        return chat;
    }
    
    /**
     * Retrieves the public connection to the database.
     * 
     * @return The public connection
     */
    public Connection getPublicConnection() {
		return this.publicConnection;
	}
}
