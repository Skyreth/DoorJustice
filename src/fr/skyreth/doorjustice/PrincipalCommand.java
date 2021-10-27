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
	
	public PrincipalCommand(Main main)
	{
		this.main = main;
		this.con = main.getMapConnexion();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) 
	{
		List<String> cmds = new ArrayList<String>();
		
		if(sender instanceof Player) 
		{
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("game") && p.hasPermission("sky.game.config")) 
			{
				if(args.length == 0) {
					cmds.add("help");
					cmds.add("create");
					cmds.add("setspawn");
					cmds.add("setlobby");
					cmds.add("setlobby");
					cmds.add("setbloc");
					cmds.add("setline");
					return cmds;
				}
			}
		}
		
		return cmds;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)  
	{
		if(sender instanceof Player) 
		{
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("game") && p.hasPermission("sky.game.config")) 
			{
				if(args.length == 0) {
					return false;
            	}
				else
				{
					switch(args[0])
					{
						case "create":
							if(args.length >= 1) {
								return false;
							}
							else {
								con.createNewTable("`"+args[1]+"`", "`SPAWN` TEXT, `LOBBY` TEXT, `BLOCK` TEXT, `LINE` TEXT");
								p.sendMessage("Vous venez de crée une nouvelle map de Justice Door nommer: "+args[1]+" !");
								return true;
							}
						case "setspawn":
							if(args.length >= 1) {
								return false;
							}
							else {
								con.insert("INSERT INTO "+args[1]+"(SPAWN) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
								p.sendMessage("Le spawn de la map "+args[1]+" a été set a votre position !");
								Location loc = p.getLocation();
								p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
								return true;
							}
						case "setlobby":
							if(args.length >= 1) {
								return false;
							}
							else {
								con.insert("INSERT INTO "+args[1]+"(LOBBY) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
								p.sendMessage("Le lobby de la map "+args[1]+" a été set a votre position !");
								Location loc = p.getLocation();
								p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
								return true;
							}
						case "setbloc":
							if(args.length >= 1) {
								return false;
							}
							else {
								con.insert("INSERT INTO "+args[1]+"(BLOCK) VALUES(?)", main.getRegUtil().serializeLocation(p.getLocation(), ","));
								p.sendMessage("Le bloc de la map "+args[1]+" a été set a votre position !");
								Location loc = p.getLocation();
								p.sendMessage("Position[world:" + loc.getWorld().getName() +", x:"+loc.getX()+", y:"+loc.getY()+", z:" + loc.getZ() +"]");
								return true;
							}
						case "setline":
							if(args.length >= 1) {
								return false;
							}
							else {
								p.sendMessage("Cassez un bloc pour definir le debut et la fin de votre zone !");
								return true;
							}
						case "deletemap":
							if(args.length >= 1) {
								return false;
							}
							else {
								this.con.deleteTable(args[1]);
								p.sendMessage("La Map "+args[1]+" a été supprimer avec succes !");
								return true;
							}
						case "help":
					}
				}
			}
		}
		return false;
	}
}
