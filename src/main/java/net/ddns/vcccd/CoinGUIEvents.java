package net.ddns.vcccd;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CoinGUIEvents implements Listener{
	
	//Returns a Boolean to see if the specific material was clicked
	private boolean checkMaterial(ItemStack item, Material material) {
		return(item.getType().equals(material));
	}
	
	//Used to round doubles in the player message
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	//Accesses the Lore of the object and returns the price as a double
	private double GetPrice(ItemMeta ClickedItemData) {
		ArrayList < String > Data = (ArrayList<String>) ClickedItemData.getLore();
		String PrimaryValue = Data.get(1);
		String[] SplitValueArray = PrimaryValue.split(" ");
		String FloatingPointString = SplitValueArray[1].substring(3);
		Double ReturnValue = Double.parseDouble(FloatingPointString);
		return(ReturnValue);
	}
	
	//Used when a purchase is made
	private void PurchaseCoin(Player player, double Price, double Balance) {
		
		//Checks the player balance compared to the price
		if(Balance < Price) {
			
			//If we don't have enough money, we return the Insufficient Funds message
			player.sendMessage(ChatColor.RED + "Insufficient Funds");
		} else {
			
			//Otherwise, we withdraw the money and update the wallet
			Main.getEconomy().withdrawPlayer(player, Price);
			player.sendMessage(ChatColor.WHITE + "You Now Have" + ChatColor.GREEN + " $" + round(Main.getEconomy().getBalance(player), 2) + ChatColor.WHITE + " Left In Your Account...");
		}
	}
	
	//Used to gather data on the pane selected for purchase or sale
	
	@EventHandler
	public void onMenuSelect(InventoryClickEvent event) {
		
		//Used to handle errors returned from unforeseen events
		try {
			
			//Checks to see if we are in the buy and sell window
			if(event.getView().getTitle().equals("Buy | Sell")) {
				
				//If we are, we get the clicked item and its data plus the player who clicked
				ItemStack ClickedItem = event.getCurrentItem();
				ItemMeta ClickedItemData = ClickedItem.getItemMeta();
				Player player = (Player) event.getWhoClicked();
				
				//We see what material was clicked in the interaction
				if(checkMaterial(ClickedItem, Material.LIME_STAINED_GLASS_PANE)) {
					
					//Economy Update and YAML File for player data
					PurchaseCoin(player, GetPrice(ClickedItemData), Main.getEconomy().getBalance(player));
					player.closeInventory();
					
				} else if(checkMaterial(ClickedItem, Material.YELLOW_STAINED_GLASS_PANE)) {
					
					player.sendMessage(Double.toString(GetPrice(ClickedItemData)));
					player.closeInventory();
					
				}else {
				}
					player.closeInventory();
				}
		} catch (Exception e) {
			assert true;
		}
		
	}

}
