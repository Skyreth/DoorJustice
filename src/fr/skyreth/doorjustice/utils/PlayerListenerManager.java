package fr.skyreth.doorjustice.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.skyreth.doorjustice.Main;

public class PlayerListenerManager implements Listener 
{
	private Main main;
	private List<Player> players = new ArrayList<Player>();
	
	public PlayerListenerManager(Main main) 
	{
		this.main = main;
	}
	
	public void addPlayer(Player p) {
		players.add(p);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	private void onPlayerDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			if(players.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
}
