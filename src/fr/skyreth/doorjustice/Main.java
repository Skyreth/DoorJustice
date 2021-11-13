package fr.skyreth.doorjustice;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import fr.skyreth.doorjustice.listeners.BreakerListener;
import fr.skyreth.doorjustice.listeners.GameListener;
import fr.skyreth.doorjustice.listeners.LobbyListener;
import fr.skyreth.doorjustice.utils.RegionUtil;
import fr.skyreth.doorjustice.utils.Utils;

public class Main extends JavaPlugin
{
	private Logger log = getLogger();
	private Connexion maps;
	private Server serv = getServer();
	private RegionUtil regutil;
	private LobbyListener manag;
	private Utils util;
	private GameListener gamelist;
	
	@Override
	public void onEnable() 
	{
		util = new Utils();
		regutil = new RegionUtil();
		maps = new Connexion("Maps.db");
		maps.connect();
		manag = new LobbyListener(this);
		gamelist = new GameListener(this);
		PluginCommand game = getCommand("game");
		PrincipalCommand cmd = new PrincipalCommand(this);
		game.setExecutor(cmd);
		game.setTabCompleter(cmd);
		serv.getPluginManager().registerEvents(new BreakerListener(this), this);
		serv.getPluginManager().registerEvents(gamelist, this);
		serv.getPluginManager().registerEvents(manag, this);
		maps.createNewTable("`game`","`NAME` TEXT, `SPAWN` TEXT, `LOBBY` TEXT, `BLOCK` TEXT, `LINE` TEXT, `SPAWNCP9` TEXT");
		log.log(Level.INFO, "Le plugin de Justice Door a démarrer avec succes !");
	}

	@Override
	public void onLoad() {
		log.log(Level.INFO, "Le plugin de Justice Door est entrain de ce lancer ...");
	}

	@Override
	public void onDisable() {
		log.log(Level.INFO, "Le plugin de Justice Door est desactiver !");
	}
	
	public Utils getUtil() {
		return util;
	}
	
	public GameListener getGameListener() {
		return gamelist;
	}
	
	public LobbyListener getPlayerListener() {
		return manag;
	}
	
	public Connexion getMapConnexion() {
		return maps;
	}
	
	public RegionUtil getRegUtil() {
		return regutil;
	}
}
