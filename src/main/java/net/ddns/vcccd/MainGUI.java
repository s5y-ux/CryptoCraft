package net.ddns.vcccd;

import net.ddns.vcccd.CryptoMenuOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

//Constructor for the MainGUI or head node in the Menu Linked List
@SuppressWarnings("unused")
public class MainGUI {

    //Creates the Inventory to be used as the GUI
    private Inventory CryptoCurrencyMainMenu = Bukkit.createInventory(null, 54, "Cryptocurrency");

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

            //Passes that map to the Option class and returns its item representation (Refer to the class)
            CryptoCurrencyMainMenu.setItem(i, new CryptoMenuOption(reference).presentItem());
        }

        //Adds GUI functionality by including extra resources
        ItemStack[] menuItems = {
            new MenuBarObject(Material.EMERALD, ChatColor.GREEN, "Rich Market Data").presentItem(),
            new MenuBarObject(Material.BOOK, ChatColor.GOLD, "Cryptocurrency News").presentItem(),
            new MenuBarObject(Material.FEATHER, ChatColor.YELLOW, "Learn").presentItem()
        };

        //Sets thoes items at the bottom of the GUI
        for (int i = 45; i <= 53 && i - 45 < menuItems.length; i++) {
            CryptoCurrencyMainMenu.setItem(i, menuItems[i - 45]);
        }

        //For all of the empty spaces, it becomes filled with glass panes.
        for (int x = 48; x < 53; x++) {
            CryptoCurrencyMainMenu.setItem(x, new MenuBarObject(Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK, ".").presentItem());
        }
        
        //Adds the exit button
        CryptoCurrencyMainMenu.setItem(53, new MenuBarObject(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Exit").presentItem());
        
        //Finally it returns the completed Inventory
        return (CryptoCurrencyMainMenu);
    }

}