package fr.skyreth.doorjustice;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import fr.skyreth.doorjustice.utils.PlayerListenerManager;
import fr.skyreth.doorjustice.utils.RegionUtil;

public class Main extends JavaPlugin
{
	private Logger log = getLogger();
	private Connexion maps;
	private Server serv = getServer();
	private RegionUtil regutil;
	private PlayerListenerManager manag;
	
	@Override
	public void onEnable() 
	{
		regutil = new RegionUtil();
		maps = new Connexion("Maps.db");
		maps.connect();
		manag = new PlayerListenerManager(this);
		PluginCommand game = getCommand("game");
		PrincipalCommand cmd = new PrincipalCommand(this);
		game.setExecutor(cmd);
		game.setTabCompleter(cmd);
		serv.getPluginManager().registerEvents(new BreakerListener(this), this);
		serv.getPluginManager().registerEvents(manag, this);
		log.log(Level.INFO, "Le plugin de Justice Door a démarrer avec succes !");
	}

	@Override
	public void onLoad() {
		log.log(Level.INFO, "Le plugin de Justice Door est entrain de ce lancer ...");
	}

	@Override
	public void onDisable() 
	{
		log.log(Level.INFO, "Le plugin de Justice Door est desactiver !");
	}
	
	public PlayerListenerManager getPlayerListener()
	{
		return manag;
	}
	
	public Connexion getMapConnexion()
	{
		return maps;
	}
	
	public RegionUtil getRegUtil()
	{
		return regutil;
	}
}
