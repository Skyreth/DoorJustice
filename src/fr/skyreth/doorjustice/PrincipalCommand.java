package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
					p.sendMessage("--------- §l[Voici les commandes]§r ---------");
					for(String cm : cmds) {
						p.sendMessage("- /game "+cm+" ");
					}
					p.sendMessage("--------- §l[Made by SkyrethTM]§r ---------");
					return false;
            }
			else if(args.length > 1) 
			{
				Location loc = p.getLocation();
				
				switch(args[0])
				{
					case "create":
							con.createNewTable("`"+args[1]+"`", "`SPAWN` TEXT, `LOBBY` TEXT, `BLOCK` TEXT, `LINE` TEXT");
							p.sendMessage("Vous venez de crée une nouvelle map de Justice Door nommer: "+args[1]+" !");
							break;
					case "join":
						if(!hasData(args[1], "LOBBY"))
						{
							new LobbyRunnable(main, args[1]);
						}
						else
							p.sendMessage("Le spawn a déja été définie pour cette map !");
						break;		
					case "setspawn":
						if(!hasData(args[1], "LOBBY"))
						{
							con.insert("INSERT INTO "+args[1]+"(SPAWN) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
							p.sendMessage("Le spawn de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
						}
						else
							p.sendMessage("Le spawn a déja été définie pour cette map !");
						break;
					case "setlobby":
						if(!hasData(args[1], "LOBBY"))
						{
							con.insert("INSERT INTO "+args[1]+"(LOBBY) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
							p.sendMessage("Le lobby de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
						}
						else
							p.sendMessage("Le lobby a déja été définie pour cette map !");
						break;
					case "setbloc":
						if(!hasData(args[1], "BLOCK"))
						{
							con.insert("INSERT INTO "+args[1]+"(BLOCK) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
							p.sendMessage("Le bloc de la map "+args[1]+" a été set a votre position !");
							p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
						}
						else
							p.sendMessage("Le bloc a déja été définie pour cette map !");
						break;
					case "setline":
						if(!hasData(args[1], "LINE"))
						{
							BreakerListener.config.add(new BlockBreakedInfos(p.getUniqueId(), args[1]));
						}
						else
							p.sendMessage("La ligne a déja definis pour cette map !");
						break;
					case "deletemap":
						if(!hasData(args[1], "LINE"))
						{
							this.con.deleteTable(args[1]);
							p.sendMessage("La Map "+args[1]+" a été supprimer avec succes !");
						}
						else
							p.sendMessage("La map "+args[1]+" n'éxiste pas !");
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
	
	private Boolean hasData(String map, String data)
	{
		String result = main.getMapConnexion().select(map, data);
		if(result != null && result != "nodata")
			return true;
		else
			return false;
	} 
}
