package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitTask;

public class LobbyRunnable 
{
	private BukkitTask task;
	private Main main;
	private Connexion con;
	private List<Player> players = new ArrayList<Player>();
	private int i = 30;
	private Location lobby;
	private String map;
	
	public LobbyRunnable(Main main, String map) {
		this.map = map;
		this.main = main;
		this.con = main.getMapConnexion();
		this.lobby = getLobbyLocation();
	}
	
	public void addPlayer(Player p) {
		if(players.size() <= 12) {
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(lobby, TeleportCause.COMMAND);
			main.getPlayerListener().addPlayer(p);
			p.sendMessage("[JusticeDoor]: Encore "+(12-players.size())+" joueurs pour commencer la partie !");
			
			if(players.size() >= 6) {
				this.startLobbyRunnable(p);
			}
		}
		else {
			p.sendMessage("§4§lErreur§r cette partie est a déja commencer !");
		}
	}
	
	public void startLobbyRunnable(Player p)
	{
		task = Bukkit.getScheduler().runTaskTimer(main, () -> 
		{
			if(i == 60)
	    		BroadCastMessage("[JusticeDoor]: La partie commence dans 1 minutes !");
	    	if(i <= 10)
	    		BroadCastMessage("[JusticeDoor]: La partie commence dans "+i+" secondes !");
	        if(i == 0) {
	        	main.getPlayerListener().removePlayer(p);
	        	// runnable de la partie 
	        	cancelTask();
	        }
	        
	        --i;
        }, 20L, 20L);
	}
	
	private void BroadCastMessage(String mess) {
		for(Player p : players) {
			p.sendMessage(mess);
		}
	}
	
	private Location getLobbyLocation()
	{
		String result = con.select(map,"LOBBY");
		
		if(result != null && result != "nodata")
			return main.getRegUtil().deserializeLocation(result, ",");
		else
			return Bukkit.getWorld("Lobby").getSpawnLocation();
	} 
	
	public void cancelTask()
	{
		if(task != null)
			task.cancel();
	}
}
