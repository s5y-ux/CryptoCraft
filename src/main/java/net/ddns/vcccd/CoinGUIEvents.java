package net.ddns.vcccd;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class CoinGUIEvents implements Listener {

    private final Main main;

    public CoinGUIEvents(Main main) {
        this.main = main;
    }

    // Check if the ItemStack matches the specified Material
    private boolean checkMaterial(ItemStack item, Material material) {
        return item != null && item.getType().equals(material);
    }

    // Round a double value to the specified number of decimal places
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        return (double) Math.round(value * factor) / factor;
    }

    // Retrieve the price from the lore of the clicked item
    // Again, we are trying to probe the API as little as possible
    private double GetPrice(ItemMeta ClickedItemData) {
        List < String > lore = ClickedItemData.getLore();
        
        //TODO I wrote this a long time ago, I'd imagine if lore is null < 2.
        //I'm going to come back and see if I can simplify this
        if (lore != null && lore.size() > 2) {
        	
        	//We get the price via the Lore
            String primaryValue = lore.get(2);
            String[] splitValueArray = primaryValue.split(" ");
            
            if (splitValueArray.length > 1) {
            	
            	//Then we get the non color coded price (String)
                String floatingPointString = splitValueArray[1].substring(3);
                
                try {
                	
                	//We parse this into a usable value with the economy
                    return Double.parseDouble(floatingPointString);
                } catch (NumberFormatException e) {
                	
                    // Log error and handle it accordingly
                	main.getConsole().sendMessage(main.prefix + ChatColor.RED + "Error -> CoinGUIEvents class " + ChatColor.YELLOW + "Contact Developer...");
                	main.getConsole().sendMessage(main.prefix + "GetPrice()");
                	return 0.0;
                 
                }
            }
        }
        return 0.0; // Return default value if parsing fails or lore is not as expected
        //This is very unlikely to happen
    }

    // Sell the specified amount of coins and add the corresponding price to the player's balance
    private void SellCoin(Player player, ItemMeta coin) {
    	
    	//Referencing the lore once again (Minimal API Calls)
        List < String > lore = coin.getLore();
        if (lore != null && lore.size() >= 2) {
        	
        	// We get the amount of coins to buy
            String amountString = lore.get(1).split(" ")[1].substring(2);
            int amount = Integer.parseInt(amountString);
            
            // We get the acronym for the coin (For the database)
            String CoinCode = lore.get(0).substring(2);
            
            // We then get the price of the coin with the method
            double coinPrice = GetPrice(coin);

            // Instantiate the database information TODO (Will probably revise this system in a new update)
            @SuppressWarnings("unused")
			PlayerData playerCoinData = new PlayerData(main, player);
            
            // Finally we call our separate class for handling database transactions
            if (new JsonQuantityRemove(player, CoinCode, amount, main).isSuccessful()) {
            	
                // Deposit the coin price to player's balance
                Main.getEconomy().depositPlayer(player, coinPrice);
                
                // Notify the player about the transaction
                player.sendMessage(main.prefix + ChatColor.GREEN + "$" + round(coinPrice, 2) + ChatColor.WHITE + " has been added to your account!");
            }
        }
    }

    // Purchase the clicked coin if the player has sufficient funds
    private void PurchaseCoin(Player player, double Price, double Balance, ItemMeta coin) {
        if (Balance < Price) {
            // Notify the player about insufficient funds
            player.sendMessage(main.prefix + ChatColor.RED + "Insufficient Funds");
        } else {
            // Withdraw the price from player's balance
            Main.getEconomy().withdrawPlayer(player, Price);
            
         // Instantiate the database information TODO (Will probably revise this system in a new update)
            @SuppressWarnings("unused")
            PlayerData playerCoinData = new PlayerData(main, player);
            
            //Once again, retrieves the lore from the item stack
            List < String > lore = coin.getLore();
            if (lore != null && lore.size() >= 2) {
            	
            	// Gets the amount of the coin purchased from the lore
                String amountString = lore.get(1).split(" ")[1];
                
                //parses the amount without the color code
                int amount = Integer.parseInt(amountString.substring(2));

                // Adds the quantity when the class is instantiated TODO (I need to learn more about SQLite and how to connect to the database
                // and employ different methods through one connection.)
                new JsonQuantityAdd(player, lore.get(0).substring(2), amount, main);

                // Notify the player about the purchase and remaining balance
                player.sendMessage(main.prefix + ChatColor.WHITE + "You Now Have" + ChatColor.GREEN + " $" + round(Main.getEconomy().getBalance(player), 2) + ChatColor.WHITE + " Left In Your Account...");
            }
        }
        
        /*
         * One thing to note about this method is the use of
         * the instantiation of the Json methods. The purchase
         * coin method has four parameters while the sell has two.
         * This is because the players balance and the price are
         * set as parameters TODO in a new update, make the purchase
         * have two like the sell coin and use getter methods for the
         * balance and the conversion method for the coin price. This
         * could help if I ever decide to go open source.
         */
    }

    // Handle inventory click events
    @EventHandler
    public void onMenuSelect(InventoryClickEvent event) {
        try {
            // Check if the clicked inventory is the coin GUI and the clicked item is not null
            if ("Buy | Sell".equals(event.getView().getTitle()) && event.getCurrentItem() != null) {
                ItemStack ClickedItem = event.getCurrentItem();
                ItemMeta ClickedItemData = ClickedItem.getItemMeta();
                Player player = (Player) event.getWhoClicked();

                // Check if the clicked item is a buy or sell pane
                if (checkMaterial(ClickedItem, Material.LIME_STAINED_GLASS_PANE)) {
                	
                    // Retrieve price and balance, then attempt to purchase
                	// TODO as said before, just allow the PurchaseCoin method to get price and balance
                	// from the player and ItemMeta...
                    double price = GetPrice(ClickedItemData);
                    double balance = Main.getEconomy().getBalance(player);
                    PurchaseCoin(player, price, balance, ClickedItemData);
                    
                } else if (checkMaterial(ClickedItem, Material.YELLOW_STAINED_GLASS_PANE)) {
                	
                    // If clicked item is a sell pane, attempt to sell coins
                    SellCoin(player, ClickedItemData);
                }
                // Close the inventory after processing the click
                player.closeInventory();
            }
        } catch (Exception e) {
            // Properly log or handle any unexpected exceptions
        	main.getConsole().sendMessage(main.prefix + ChatColor.RED + "Error -> CoinGUIEvents class " + ChatColor.YELLOW + "Contact Developer...");
        	main.getConsole().sendMessage(main.prefix + "onMenuSelect()");
        }
    }
}