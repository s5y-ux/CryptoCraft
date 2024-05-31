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
    private String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&f[&eCryptoCraft&f] ");

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
    private double GetPrice(ItemMeta ClickedItemData) {
        List < String > lore = ClickedItemData.getLore();
        if (lore != null && lore.size() > 2) {
            String primaryValue = lore.get(2);
            String[] splitValueArray = primaryValue.split(" ");
            if (splitValueArray.length > 1) {
                String floatingPointString = splitValueArray[1].substring(3);
                try {
                    return Double.parseDouble(floatingPointString);
                } catch (NumberFormatException e) {
                    // Log error or handle it accordingly
                    return 0.0;
                }
            }
        }
        return 0.0; // Return default value if parsing fails or lore is not as expected
    }

    // Sell the specified amount of coins and add the corresponding price to the player's balance
    private void SellCoin(Player player, ItemMeta coin) {
        List < String > lore = coin.getLore();
        if (lore != null && lore.size() >= 2) {
            String amountString = lore.get(1).split(" ")[1].substring(2);
            int amount = Integer.parseInt(amountString);
            String CoinCode = lore.get(0).substring(2);
            double coinPrice = GetPrice(coin);

            PlayerData playerCoinData = new PlayerData(main, player);
            if (new JsonQuantityRemove(player, CoinCode, amount, main).isSuccessful()) {
                // Deposit the coin price to player's balance
                Main.getEconomy().depositPlayer(player, coinPrice);
                // Notify the player about the transaction
                player.sendMessage(pluginPrefix + ChatColor.GREEN + "$" + round(coinPrice, 2) + ChatColor.WHITE + " has been added to your account!");
            }
        }
    }

    // Purchase the clicked coin if the player has sufficient funds
    private void PurchaseCoin(Player player, double Price, double Balance, ItemMeta coin) {
        if (Balance < Price) {
            // Notify the player about insufficient funds
            player.sendMessage(pluginPrefix + ChatColor.RED + "Insufficient Funds");
        } else {
            // Withdraw the price from player's balance
            Main.getEconomy().withdrawPlayer(player, Price);
            PlayerData playerCoinData = new PlayerData(main, player);
            List < String > lore = coin.getLore();
            if (lore != null && lore.size() >= 2) {
                String amountString = lore.get(1).split(" ")[1];
                int amount = Integer.parseInt(amountString.substring(2));

                new JsonQuantityAdd(player, lore.get(0).substring(2), amount, main);
                //playerCoinData.Purchase(lore.get(0).substring(2), amount);

                player.sendMessage("Test");
                // Notify the player about the purchase and remaining balance
                player.sendMessage(pluginPrefix + ChatColor.WHITE + "You Now Have" + ChatColor.GREEN + " $" + round(Main.getEconomy().getBalance(player), 2) + ChatColor.WHITE + " Left In Your Account...");
            }
        }
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
        }
    }
}