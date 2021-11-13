package fr.skyreth.doorjustice;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class GameManager 
{
	private BukkitTask task;
	private Main main;
	private Game game;
	
	
	public GameManager(Main main, Game game)
	{
		this.main = main;
		
		task = Bukkit.getScheduler().runTaskTimer(main, () -> 
		{
			if(game.isStop()) {
				cancelTask();
			}
        }, 20L, 20L);
	}
	
	public void cancelTask() {
		if(task != null)
			task.cancel();
	}
}
