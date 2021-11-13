package fr.skyreth.doorjustice.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.skyreth.doorjustice.Main;

public class LobbyListener implements Listener 
{
	private Main main;
	private List<Player> players = new ArrayList<Player>();
	
	public LobbyListener(Main main) {
		this.main = main;
	}
	
	public void addPlayer(Player p) {
		players.add(p);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	@EventHandler
	private void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			if(players.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	private void onFoodLeveChange(FoodLevelChangeEvent e) {
		if(e.getEntityType() == EntityType.PLAYER)
		{
			Player p = (Player) e.getEntity();
			if(players.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
}
