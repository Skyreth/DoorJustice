package fr.skyreth.doorjustice.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Region2D 
{
	private World world;
	private int maxX, minX, maxZ, minZ;
	
	public Region2D(Location loc1, Location loc2) {
        this(loc1.getWorld(), loc1.getBlockX(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockZ());
	}
	
	public Region2D(World world, int x1, int x2, int z1, int z2) {
        this.world = world;

        minX = Math.min(x1, x2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxZ = Math.max(z1, z2);
    }
	
	public World getWorld() {
	    return world;
	}

    public int getMinX() {
	    return minX;
	}

	public int getMinZ() {
	    return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxZ() {
	    return maxZ;
	}
	
    public String toString() {
        return "Region[world:" + world.getName() + ", minX:" + minX + ", minZ:" + minZ +", maxX:" + maxX +", maxZ:" + maxZ + "]";
    }
    
    public String serialize(String sep) {
    	return world.getName()+sep+minX+sep+maxX+sep+minZ+sep+maxZ;
    }
    
    public boolean contains(Region2D region) {
        return region.getWorld().equals(world) && region.getMinX() >= minX && region.getMaxX() <= maxX && region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
    }
}
