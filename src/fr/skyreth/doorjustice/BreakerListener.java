package fr.skyreth.doorjustice;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.skyreth.doorjustice.utils.Region2D;

public class BreakerListener implements org.bukkit.event.Listener
{
	public HashMap<UUID, BlockBreakedInfos> config = new HashMap<UUID, BlockBreakedInfos>();
	private Main main;
	
	public BreakerListener(Main main)
	{
		this.main = main;
	}
	
	@EventHandler
	private void OnBlockBreak(BlockBreakEvent e)
	{
		if(config.containsKey(e.getPlayer().getUniqueId())) {
			if(config.get(e.getPlayer().getUniqueId()).getBlock().isEmpty()) {
				config.get(e.getPlayer().getUniqueId()).setBlock(e.getBlock());
				e.getPlayer().sendMessage("Vous venez de définir le début de votre selection !");
			}
			else {
				e.getPlayer().sendMessage("Vous venez de définir la fin de votre zone !");
				main.getMapConnexion().insert("INSERT INTO "+config.get(e.getPlayer().getUniqueId()).getMap()+"(LINE) VALUES(?)", new Region2D(config.get(e.getPlayer().getUniqueId()).getBlock().getLocation(), e.getBlock().getLocation()).serialize(","));
				config.remove(e.getPlayer().getUniqueId());
			}
		}
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