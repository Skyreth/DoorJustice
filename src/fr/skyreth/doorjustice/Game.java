package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import fr.skyreth.doorjustice.utils.Region2D;
import fr.skyreth.doorjustice.utils.Utils;

public class Game 
{
	private String map;
	private Main main;
	private Utils util;
	private List<Player> pirates, cp9, players;
	private HashMap<Player, Integer> rplayers;
	private GameState state;
	private boolean stop;
	private Region2D line;
	private Location block, lobby, spawn, spawncp9;
	private Connexion con;
	private BukkitTask task;
	private int i = 30;
	
	public Game(Main main, String map) {
		this.main = main;
		this.con = main.getMapConnexion();
		this.util = main.getUtil();
		this.map = map;
		this.line = getLineInDB();
		this.stop = false;
		this.state = GameState.ATTENTE;
		this.lobby = getLobbyLocation();
		this.spawn = getSpawnInDB();
		this.spawncp9 = getCP9SpawnInDB();
		pirates = new ArrayList<Player>();
		cp9 = new ArrayList<Player>();
		players = new ArrayList<Player>();
	}
	
	public void addPlayer(Player p) {
		if(players.size() < 12) {
			p.setGameMode(GameMode.ADVENTURE);
			p.setFoodLevel(20);
			p.teleport(getLobbyLocation(), TeleportCause.COMMAND);
			players.add(p);
			main.getPlayerListener().addPlayer(p);
			main.getUtil().BroadCastMessage(players, "[JusticeDoor]: Encore "+(6-players.size())+" joueurs pour commencer la partie !");
			main.getUtil().BroadCastMessage(players, "[JusticeDoor]: "+p.getName()+" a rejoins la game !");
			
			if(players.size() >= 4) {
				startLobbyRunnable();
			}
		}
	}
	
	public void removePlayer(Player p) {
		if(players.contains(p)) {
			players.remove(p);
		}
		
		if(cp9.contains(p)) {
			cp9.remove(p);
		}
		
		if(pirates.contains(p)) {
			pirates.remove(p);
		}
	}
	
	private void startLobbyRunnable()
	{
		task = Bukkit.getScheduler().runTaskTimer(main, () -> 
		{
	    	main.getUtil().BroadCastMessage(players, "[JusticeDoor]: La partie commence dans "+i+" secondes !");
	    	
	        if(i == 0) {
	        	state = GameState.DEMARRE;
	        	setupTeam();
	    		setupRoles();
	    		
	    		for(Player p : pirates) {
	    			p.teleport(spawn, TeleportCause.PLUGIN);
	    		}
	    		
	    		for(Player p : cp9) {
	    			p.teleport(main.getUtil().randomLocationInRadius(spawncp9, 2), TeleportCause.PLUGIN);
	    		}
	    		
	    		for(Player ps : players) {
	    			giveStuff(ps);
	        		main.getPlayerListener().removePlayer(ps);
	        	}
	    		
	    		new GameManager(main, this);
	        	cancelTask();
	        }
	        
	        --i;
        }, 20L, 20L);
	}
	
	private void cancelTask() {
		if(task != null)
			task.cancel();
	}
	
	private Location getLobbyLocation() {
		String result = con.selectWhere("game", "NAME", map, "LOBBY");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserializeLocation(result, ",");
		else
			return Bukkit.getWorld("Lobby").getSpawnLocation();
	} 
	
	private Region2D getLineInDB() {
		String result = main.getMapConnexion().selectWhere("game", "NAME", map, "LINE");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserialize(result, ",");
		else
			return null;
	}
	
	private Location getBlockInDB() {
		String result = main.getMapConnexion().selectWhere("game", "NAME", map, "BLOCK");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserializeLocationWithLook(result, ",");
		else
			return null;
	}
	
	private Location getSpawnInDB() {
		String result = main.getMapConnexion().selectWhere("game", "NAME", map, "SPAWN");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserializeLocation(result, ",");
		else
			return null;
	}
	private Location getCP9SpawnInDB() {
		String result = main.getMapConnexion().selectWhere("game", "NAME", map, "SPAWNCP9");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserializeLocation(result, ",");
		else
			return null;
	}
	
	public GameState getState() {
		return state;
	}
	
	public void setState(GameState st) {
		this.state = st;
	}
	
	public Location getBlockLocation() {
		return block;
	}
	
	public String getMap() {
		return map;
	}
	
	public Region2D getLine() {
		return line;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public boolean isStop() {
		return stop;
	}
	
	public void StopGame() {
		this.stop = true;
	}
	
	public List<Player> getPirates() {
		return pirates;
	}
	
	public List<Player> getCp9() {
		return cp9;
	}
	
	private void setupRoles()
	{
		rplayers = new HashMap<Player, Integer>();
		Collections.shuffle(cp9);
		Collections.shuffle(pirates);
		
		for(int i=0;i<cp9.size();i++) {
			Player p = cp9.get(i);
			rplayers.put(p, i+7);
			p.sendMessage("[JusticeDoor]: Vous etes "+getRoleByIndex(i+7));
		}
		
		for(int i=0;i<pirates.size();i++) {
			Player p = pirates.get(i);
			rplayers.put(p, i+1);
			p.sendMessage("[JusticeDoor]: Vous etes "+getRoleByIndex(i+1));
		}
	}
	
	public String getPlayerRole(Player p) {
		return getRoleByIndex(rplayers.get(p));
	}
	
	public int getPlayerRoleIndex(Player p) {
		return rplayers.get(p);
	}
	
	private String getRoleByIndex(int index) {
		switch(index) {
			case 1:
				return "Luffy";
			case 2:
				return "Zoro";
			case 3:
				return "Sanji";
			case 4:
				return "Nami";
			case 5:
				return "Chopper";
			case 6:
				return "Francky";
			case 7:// bad camp
				return "Rob Lucci";
			case 8:
				return "Kaku";
			case 9:
				return "Jabra";
			case 10:
				return "Blueno";
			case 11:
				return "Kalifa";
			case 12:
				return "Nero";
		}
		return null;
	}
	
	public void getPowerByPlayerRole(Player p) {
		String r = getRoleByIndex(getPlayerRoleIndex(p));
		
		switch(r)
		{
			case "Luffy":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 2), false);
				break;
			case "Zoro":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, 1), false);
				break;
			case "Sanji":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, 1), false);
				break;
			case "Nami":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, 1), false);
				break;
			case "Chopper":
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 2), false);
				break;
			case "Franky":
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 1), false);
				break;
			case "Rob Lucci":
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15, 1), false);
				break;
			case "Kaku":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 1), false);
				break;
			case "Jabra":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 1), false);
				break;
			case "Blueno":
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, 1), false);
				break;
			case "Kalifa":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15, 1), false);
				break;
			case "Nero":
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 2), false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 1), false);
				break;
		}
	}
	
	private void setupTeam() {
		List<Player> c = new ArrayList<Player>(players);
		Random random = new Random();
		
		if(c != null)
		{
			for(int i=0;i<c.size();i++) {
				Player p = c.get(i);
				
				if(cp9.size() == pirates.size()) 
				{	
					if(random.nextBoolean()) {
					    pirates.add(p);
					    p.sendMessage("[JusticeDoor]: Vous etes désormais pirates !");
					} 
					else {
					    cp9.add(p);
					    p.sendMessage("[JusticeDoor]: Vous etes désormais CP9 !");
					}
				}
				else
				{
					 if(cp9.size() < pirates.size()) {
						 cp9.add(p);
						 p.sendMessage("[JusticeDoor]: Vous etes désormais CP9 !");
					 }
					 else
					 {
						 pirates.add(p);
						 p.sendMessage("[JusticeDoor]: Vous etes désormais pirates !");
					 }
				}
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
		p.setGameMode(GameMode.SURVIVAL);
		for(int i=5;i!=0;i--) {
			p.getInventory().addItem(new ItemStack(Material.WOOD, 64));
		}
		
		if(cp9.contains(p)) {
			p.getInventory().setItem(5, main.getUtil().CreateItemCustom("§lPas de Lune", Material.FEATHER, 1));
		}
	}
}
