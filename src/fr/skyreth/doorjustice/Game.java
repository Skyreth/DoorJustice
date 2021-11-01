package fr.skyreth.doorjustice;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.skyreth.doorjustice.utils.Region2D;
import fr.skyreth.doorjustice.utils.Utils;

public class Game 
{
	private List<Player> players;
	private String map;
	private Main main;
	private Utils util;
	private List<Player> pirates, cp9;
	private GameState state;
	
	public Game(Main main, String map, List<Player> ps) {
		this.main = main;
		this.util = main.getUtil();
		this.map = map;
		this.state = GameState.ATTENTE;
		this.players = ps;
		setupTeam(ps);
	}
	
	public Region2D getLine(String map) {
		String result = main.getMapConnexion().select(map,"LINE");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserialize(result, ",");
		else
			return null;
	}
	
	public List<Player> getPirates() {
		return pirates;
	}
	
	public List<Player> getCp9() {
		return cp9;
	}
	
	private void setupTeam(List<Player> ps) {
		for(Player p : ps) {
			if(pirates.size() >= cp9.size()) {
			    pirates.add(p);
			    p.sendMessage("[JusticeDoor]: Vous etes désormais pirates !");
			} else if(cp9.size() <= pirates.size()) {
			    cp9.add(p);
			    p.sendMessage("[JusticeDoor]: Vous etes désormais CP9 !");
			}
		}
	}
	
	private void giveStuff(Player p) {
		p.getInventory().setHelmet(util.addEnchantToItem(new ItemStack(Material.IRON_HELMET), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY}, new int[] {3,3}));
		p.getInventory().setChestplate(util.addEnchantToItem(new ItemStack(Material.DIAMOND_CHESTPLATE), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY}, new int[] {2,3}));
		p.getInventory().setLeggings(util.addEnchantToItem(new ItemStack(Material.IRON_LEGGINGS), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY}, new int[] {3,3}));
		p.getInventory().setBoots(util.addEnchantToItem(new ItemStack(Material.DIAMOND_BOOTS), new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY}, new int[] {2,3}));
		p.getInventory().setItem(0, util.addEnchantToItem(new ItemStack(Material.DIAMOND_SWORD), new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {2}));
		p.getInventory().setItem(1, util.addEnchantToItem(new ItemStack(Material.BOW), new Enchantment[] {Enchantment.ARROW_DAMAGE}, new int[] {1}));
		p.getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
		p.getInventory().setItem(3, new ItemStack(Material.GOLDEN_APPLE, 16));
		p.getInventory().setItem(4, new ItemStack(Material.COOKED_BEEF, 64));
		for(int i=5;i!=0;i--) {
			p.getInventory().addItem(new ItemStack(Material.WOOD, 64));
		}
	}
}
