package net.ddns.vcccd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

//Constructor for the MainGUI or head node in the Menu Linked List
public class MainGUI {

    //Reference to Main for file configuration integration
    private final Main main;

    //Creates the Inventory to be used as the GUI
    private Inventory CryptoCurrencyMainMenu = Bukkit.createInventory(null, 54, "Cryptocurrency");

    //Constructs the main GUI and passes the value for main
    public MainGUI(Main main) {
        this.main = main;
    }

    /*
___  ___                   _____ _                     
|  \/  |                  |_   _| |                    
| .  . | ___ _ __  _   _    | | | |_ ___ _ __ ___  ___ 
| |\/| |/ _ \ '_ \| | | |   | | | __/ _ \ '_ ` _ \/ __|
| |  | |  __/ | | | |_| |  _| |_| ||  __/ | | | | \__ \
\_|  |_/\___|_| |_|\__,_|  \___/ \__\___|_| |_| |_|___/
                                                       
	*/
    //========================================================================================================================================================
    
    //This method is used in order to create an ItemStack with a custom name quickly
    private ItemStack MenuBarItem(Material material, ChatColor chatcolor, String text) {

        //Creates a Itemstack of the specified material
        ItemStack returnValue = new ItemStack(material);

        //Retrives the item meta and stores it
        ItemMeta meta = returnValue.getItemMeta();

        //Changes the meta to include the custom name
        meta.setDisplayName(chatcolor + text);

        //Sets the Item Meta
        returnValue.setItemMeta(meta);

        //Then returns the custom ItemStack
        return (returnValue);
    }

    //This method constructs a Crypto Currency option using a Map as designed with the public API
    private ItemStack Option(Map < ? , ? > reference) {

        //Creates an ItemStack as a Chest
        ItemStack value = new ItemStack(Material.CHEST);

        //Retrives the Item Meta
        ItemMeta meta = value.getItemMeta();

        //Creats an Empty array list for the item lore
        ArrayList < String > Lore = new ArrayList < String > ();

        //This chunk of code is used in order to format the Lore of the item holding the crypto information
        Lore.add(ChatColor.BLUE + ">-------------------<");
        Lore.add(ChatColor.WHITE + "Symbol: " + ChatColor.AQUA + reference.get("symbol").toString());
        Lore.add(ChatColor.WHITE + "Price: " + ChatColor.GREEN + "$" + reference.get("priceUsd").toString());
        if (Float.parseFloat(reference.get("changePercent24Hr").toString()) < 0) {
            Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.RED + "%" + reference.get("changePercent24Hr").toString());
        } else {
            Lore.add(ChatColor.WHITE + "Percent Change: " + ChatColor.GREEN + "%" + reference.get("changePercent24Hr").toString());
        }
        Lore.add(ChatColor.BLUE + ">-------------------<");

        //Sets the lore in the meta
        meta.setLore(Lore);

        //Stores the name of the coin into a String object
        String name = reference.get("name").toString();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&ki&f&l") + name + ChatColor.translateAlternateColorCodes('&', "&r&7&ki"));

        //Sets the meta and returns the ItemStack
        value.setItemMeta(meta);
        return (value);
    }
    //========================================================================================================================================================

    //This is the only public method besides the constructor. This is used to return the menu inventory to the player.
    //The whole point of the Java file is to build the inventory and then store the info in an inventory object.
    public Inventory getGUI() {

        //Calls the public API for crypto data
        MarketData CryptoOption = new MarketData();

        //Casts it to a List where each element is a map that holds crypto information.
        List < ? > Coins = (List < ? > ) CryptoOption.getRawData().get("data");

        //Iterates through the top 45 list elements and recieves the maps
        for (int i = 0; i < 45; i++) {

            //Creates a reference map that holds the specific coin according to the iteration
            Map < ? , ? > reference = (Map < ? , ? > ) Coins.get(i);

            //Passes that map to the Option method (Refer to the menu items)
            CryptoCurrencyMainMenu.setItem(i, Option(reference));
        }

        //Adds GUI functionality by including extra resources
        ItemStack[] menuItems = {
            MenuBarItem(Material.EMERALD, ChatColor.GREEN, "Rich Market Data"),
            MenuBarItem(Material.BOOK, ChatColor.GOLD, "Cryptocurrency News"),
            MenuBarItem(Material.FEATHER, ChatColor.YELLOW, "Learn"),
            MenuBarItem(Material.RED_WOOL, ChatColor.RED, "Exit")
        };

        //Sets thoes items at the bottom of the GUI
        for (int i = 45; i <= 53 && i - 45 < menuItems.length; i++) {
            CryptoCurrencyMainMenu.setItem(i, menuItems[i - 45]);
        }

        //For all of the empty spaces, it becomes filled with glass panes.
        for (int x = 48; x < 53; x++) {
            CryptoCurrencyMainMenu.setItem(x, MenuBarItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK, "."));
        }

        //Finally it returns the completed Inventory
        return (CryptoCurrencyMainMenu);
    }

}