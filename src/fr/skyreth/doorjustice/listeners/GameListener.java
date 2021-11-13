package fr.skyreth.doorjustice.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import fr.skyreth.doorjustice.Game;
import fr.skyreth.doorjustice.Main;

public class GameListener implements Listener
{
	private Main main;
	private List<Game> games = new ArrayList<Game>();
	private List<Player> cool = new ArrayList<Player>();
	
	public GameListener(Main main) {
		this.main = main;
	}
	
	public void addGame(Game inf) {
		games.add(inf);
	}
	
	public Game getMapByName(String name) {
		for(Game game : games) {
			if(game.getMap().equals(name)) {
				return game;
			}
		}
		return null;
	}
	
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		if(inf.getLine().contains(p.getLocation()) && inf.getPirates().contains(p)) {
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: Fin de la partie !");
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: Les pirates ont gagnée !");
		}
	}
	
	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		if(p.getInventory().contains(new ItemStack(Material.GLASS, 1, (byte)2))) {
			p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.GLASS, 1, (byte)2));
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: Le porteur du bloc de Robin est mort !");
		}
		
		if(inf.getPirates().contains(p)) {
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: un pirate est mort !");
			inf.getPirates().remove(p);
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
		
		if(inf.getCp9().contains(p)) {
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: un cp9 est mort !");
			inf.getCp9().remove(p);
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
	}
	
	@EventHandler
	private void onPlayerClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock() != null) {
				if(inf.getPirates().contains(p) && e.getClickedBlock().getType().equals(Material.GLASS) && e.getClickedBlock().getLocation().equals(inf.getBlockLocation())) {
					main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: Le bloc de Robin a été pris !");
				}
			}
		}
	}
	
	@EventHandler
	private void onPlayerClick2(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(e.getItem() != null)
			{
				if(e.getItem().getType() == Material.DIAMOND_SWORD) {
					if(cool.contains(p)) {
						p.sendMessage("[JusticeDoor]: Votre abilité est en cooldown !");
					}
					else
					{
						inf.getPowerByPlayerRole(p);
						cool.add(p);
						Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
				            public void run() {
				            	if(cool.contains(p))
				            		cool.remove(p);
				            }
				        }, 3030, 24000);
					}
				}
				if(main.getUtil().checkCustomItem(e.getItem(), "§lPa de Lune")) {
					if(!cool.contains(p)) {
						inf.getPowerByPlayerRole(p);
						cool.add(p);
						Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
				            public void run() {
				            	if(cool.contains(p))
				            		cool.remove(p);
				            }
				        }, 90, 24000);
					}
					else {
						p.sendMessage("[JusticeDoor]: Cette abilité est en cooldown !");
					}
				}
			}
		}

	}
	
	@EventHandler 
	private void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: "+p.getName()+" a quitter la game !");
		inf.removePlayer(p);
	}
	
	@EventHandler
	private void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		Game inf = getGameInfosByPlayer(p);
		
		if(e.getBlock().getType() == Material.GLASS) {
			main.getUtil().BroadCastMessage(inf.getPlayers(), "[JusticeDoor]: Le bloc de Robin a été remis !");
		}
	}
	
	@EventHandler
	private void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if(getGameInfosByPlayer(p) == null) return;
			Game inf = getGameInfosByPlayer(p);
			
			if(inf.getCp9().contains(p) && inf.getCp9().contains(d)) {
				e.setCancelled(true);
			}
			
			if(inf.getPirates().contains(p) && inf.getPirates().contains(d)) {
				e.setCancelled(true);
			}
			
			if (e.getDamager() instanceof Projectile) {
	            if ((e.getDamager() instanceof Arrow)) {
	 
	                Arrow a = (Arrow) e.getDamager();
	                if (e.getEntity() instanceof Player) {
	                    Player victim = (Player) e.getEntity();
	                    if (a.getShooter() instanceof Player) {
	                        Player damagerp = (Player) a.getShooter();
	                        
	                        if(inf.getCp9().contains(victim) && inf.getCp9().contains(damagerp)) {
	            				e.setCancelled(true);
	            			}
	            			
	            			if(inf.getPirates().contains(victim) && inf.getPirates().contains(damagerp)) {
	            				e.setCancelled(true);
	            			}
	                    }
	                }
	            }
			}
		}
	}
	
	private Game getGameInfosByPlayer(Player p) {
		for(Game inf : games) {
			if(inf.getPirates().contains(p) || inf.getCp9().contains(p))
				return inf;
		}
		return null;
	}
}
