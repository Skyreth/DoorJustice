package fr.skyreth.doorjustice.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.skyreth.doorjustice.BlockBreakedInfos;
import fr.skyreth.doorjustice.Main;
import fr.skyreth.doorjustice.utils.Region2D;

public class BreakerListener implements org.bukkit.event.Listener
{
	public static List<BlockBreakedInfos> config = new ArrayList<BlockBreakedInfos>();
	private Main main;
	
	public BreakerListener(Main main) {
		this.main = main;
	}

	@EventHandler
	private void OnBlockBreak(BlockBreakEvent e)
	{
		BlockBreakedInfos p = getPlayerInConfig(e.getPlayer());
		
		if(p != null)
		{
			if(p.getBlock() == null) {
				p.setBlock(e.getBlock());
				e.getPlayer().sendMessage("Vous venez de définir le début de votre selection !");
				e.setCancelled(true);
			}
			else {
				e.getPlayer().sendMessage("Vous venez de définir la fin de votre zone !");
				Region2D reg = new Region2D(p.getBlock().getLocation(), e.getBlock().getLocation());
				e.getPlayer().sendMessage(reg.toString());
				main.getMapConnexion().replaceDataWhere("game", "LINE", p.getMap(), reg.serialize(","), "NAME");
				config.remove(p);
				e.setCancelled(true);
			}
		}
	}
	
	private BlockBreakedInfos getPlayerInConfig(Player p)
	{
		for(BlockBreakedInfos inf : config) {
			if(inf.getUuid().equals(p.getUniqueId())) {
				return inf;
			}
		}
		
		return null;
	}
}