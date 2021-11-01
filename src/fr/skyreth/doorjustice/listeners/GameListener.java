package fr.skyreth.doorjustice.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.skyreth.doorjustice.GameInfos;
import fr.skyreth.doorjustice.Main;

public class GameListener implements Listener
{
	private Main main;
	private List<GameInfos> infos = new ArrayList<GameInfos>();
	
	public GameListener(Main main) {
		this.main = main;
	}
	
	public void addGame(GameInfos inf) {
		infos.add(inf);
	}
	
	private void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(getGameInfosByPlayer(p) == null) return;
		GameInfos inf = getGameInfosByPlayer(p);
		
		if(inf.getLine().contains(p.getLocation())) {
			
		}
	}
	
	private GameInfos getGameInfosByPlayer(Player p) {
		for(GameInfos inf : infos) {
			if(inf.getGame().getPirates().contains(p) || inf.getGame().getCp9().contains(p))
				return inf;
		}
		return null;
	}
}
