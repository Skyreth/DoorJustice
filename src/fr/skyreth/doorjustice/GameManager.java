package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import fr.skyreth.doorjustice.listeners.GameListener;

public class GameManager 
{
	private BukkitTask task;
	private Location spawn;
	private Main main;
	private List<Player> pirates = new ArrayList<Player>();
	private List<Player> cp9 = new ArrayList<Player>();
	private GameInfos infos;
	private Game game;
	
	
	public GameManager(Main main, String map, List<Player> ps)
	{
		this.main = main;
		this.game = new Game(main, map, ps);
		infos = new GameInfos(game.getLine(map), game);
		GameListener list = main.getGameListener();
		
		task = Bukkit.getScheduler().runTaskTimer(main, () -> 
		{
			// si plus de membre d'une team cancel task
        }, 20L, 20L);
	}
}
