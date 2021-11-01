package fr.skyreth.doorjustice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.skyreth.doorjustice.utils.Region2D;

public class BreakerListener implements org.bukkit.event.Listener
{
	public static List<BlockBreakedInfos> config = new ArrayList<BlockBreakedInfos>();
	private Main main;
	
	public BreakerListener(Main main)
	{
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
				main.getMapConnexion().insert("INSERT INTO "+p.getMap()+"(LINE) VALUES(?)", new Region2D(p.getBlock().getLocation(), e.getBlock().getLocation()).serialize(","));
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
class BlockBreakedInfos
{
	private Block b;
	private UUID uuid;
	private String map;
	
	public BlockBreakedInfos(UUID uuid, String map)
	{
		this.uuid = uuid;
		this.map = map;
	}

	public String getMap() {
		return map;
	}

	public Block getBlock() {
		return b;
	}

	public void setBlock(Block b) {
		this.b = b;
	}

	public UUID getUuid() {
		return uuid;
	}
}