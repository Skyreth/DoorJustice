package fr.skyreth.doorjustice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils 
{
	public int generateRandomNumber(int max, int min) {
		int al = min + (int)(Math.random() * ((max - min) + min));
		return al;
	}
	
	public void BroadCastMessage(List<Player> players, String mess) {
		players.forEach(p -> p.sendMessage(mess));
	}
	
	public ItemStack addEnchantToItem(ItemStack stack, Enchantment[] enchant, int[] level) {    
		ItemMeta meta = stack.getItemMeta();
		
		for(int i=0;i<enchant.length;i++) {
			meta.addEnchant(enchant[i], level[i], true);
		}
		stack.setItemMeta(meta);
		return stack;
	}
	
	public ItemStack addCustomLoreToItem(ItemStack stack, String name, String string) {    
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(string);
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public Location randomLocationInRadius(Location center, int radius)
	{
		Random rand = new Random();
		double angle = rand.nextDouble()*360; 
		double x = center.getX() + (rand.nextDouble()*radius*Math.cos(Math.toRadians(angle))); // x
		double z = center.getZ() + (rand.nextDouble()*radius*Math.sin(Math.toRadians(angle))); // z
		return new Location(center.getWorld(), x, center.getY()+2, z);
	}
	
	public ItemStack CreateItemCustom(String name, Material mat, int amount) {
		ItemStack item = new ItemStack(mat, amount);
		ItemMeta data = item.getItemMeta();
		data.setDisplayName(name);
		item.setItemMeta(data);
		return item;
	}
	
	public Boolean checkCustomItem(ItemStack item, String Name) {
		return (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(Name));
	}
}
