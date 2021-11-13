package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.skyreth.doorjustice.listeners.BreakerListener;

public class PrincipalCommand implements CommandExecutor, TabCompleter
{
	private Main main;
	private Connexion con;
	private List<String> cmds = new ArrayList<String>();
	
	public PrincipalCommand(Main main)
	{
		this.main = main;
		this.con = main.getMapConnexion();
		cmds.add("help");
		cmds.add("create");
		cmds.add("setspawn");
		cmds.add("setspawncp9");
		cmds.add("setlobby");
		cmds.add("setbloc");
		cmds.add("setline");
		cmds.add("deletemap");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) 
	{
		List<String> cmds = new ArrayList<String>();
		
		if(sender instanceof Player) 
		{
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("game") && p.hasPermission("skyreth.game.config")) 
				if(args.length >= 1) 
					return cmds;
		}
		
		return cmds;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)  
	{
		if(sender instanceof Player) 
		{
			Player p = (Player) sender;
			
			if(args.length == 0) {
					p.sendMessage("La commande a échouer /game help pour plus d'infos !");
					p.performCommand("/game help t");
					return false;
            }
			else if(args.length > 1) 
			{
				Location loc = p.getLocation();
				
				switch(args[0])
				{
					case "create":
							con.insert("INSERT INTO game(NAME) VALUES(?)", args[1]);
							p.sendMessage("Vous venez de crée une nouvelle map de Justice Door nommer: "+args[1]+" !");
							break;
					case "join":
						Game game = main.getGameListener().getMapByName(args[1]);
						if(game == null) {
							Game g = new Game(main, args[1]);
							g.addPlayer(p);
							p.getInventory().clear();
							main.getGameListener().addGame(g);
							break;
						}
						else if(game.getState() == GameState.ATTENTE) {
							if(!game.getPlayers().contains(p)) {
								game.addPlayer(p);
								p.getInventory().clear();
							}
							break;
						}
						else {
							p.sendMessage("§4Une game est deja lancer sur cette map !");
						}
						break;		
					case "setspawn":
							con.replaceDataWhere("game", "SPAWN", args[1], main.getRegUtil().serializeLocation(p.getLocation(), ","), "NAME");
							p.sendMessage("Le spawn de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getBlockX()+", y:"+loc.getBlockY()+", z:" + loc.getBlockZ() +"]");
						break;
					case "setspawncp9":
							con.replaceDataWhere("game", "SPAWNCP9", args[1], main.getRegUtil().serializeLocation(p.getLocation(), ","), "NAME");
							p.sendMessage("Le spawn de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getBlockX()+", y:"+loc.getBlockY()+", z:" + loc.getBlockZ() +"]");
						break;
					case "setlobby":
							con.replaceDataWhere("game", "LOBBY", args[1], main.getRegUtil().serializeLocation(p.getLocation(), ","), "NAME");
							p.sendMessage("Le lobby de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getBlockX()+", y:"+loc.getBlockY()+", z:" + loc.getBlockZ() +"]");
						break;
					case "setbloc":
							con.replaceDataWhere("game", "BLOCK", args[1], main.getRegUtil().serializeLocationWithoutLook(p.getLocation().getBlock().getLocation().subtract(0, 1, 0), ","), "NAME");
							p.sendMessage("Le bloc de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getBlockX()+", y:"+loc.getBlockY()+", z:" + loc.getBlockZ() +"]");
						break;
					case "setline":
							p.sendMessage("Cassez un bloc pour definir la selection !");
							BreakerListener.config.add(new BlockBreakedInfos(p.getUniqueId(), args[1]));
							break;
					case "deletemap":
							con.delete("game", "NAME", args[1]);
							p.sendMessage("La Map "+args[1]+" a été supprimer avec succes !");
						break;
					case "help":
						p.sendMessage("--------- §l[Voici les commandes]§r ---------");
						for(String cm : cmds) {
							p.sendMessage("- /game "+cm+" ");
						}
						p.sendMessage("--------- §l[Made by SkyrethTM]§r ---------");
						break;
				}
			}
			else
			{
				return false;
			}
		}
		return false;
	}
}
