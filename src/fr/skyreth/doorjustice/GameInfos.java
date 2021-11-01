package fr.skyreth.doorjustice;

import org.bukkit.scheduler.BukkitTask;

import fr.skyreth.doorjustice.utils.Region2D;

public class GameInfos
{
	private int id;
	private Region2D line;
	private BukkitTask task;
	private Game game;
	
	public GameInfos(Region2D line, Game game) {
		this.line = line;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Region2D getLine() {
		return line;
	}
	
	public void cancelTask() {
		if(task != null)
			task.cancel();
	}
}
