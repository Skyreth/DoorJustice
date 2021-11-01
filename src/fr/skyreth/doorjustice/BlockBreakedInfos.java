package fr.skyreth.doorjustice;

import java.util.UUID;

import org.bukkit.block.Block;

public class BlockBreakedInfos
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
