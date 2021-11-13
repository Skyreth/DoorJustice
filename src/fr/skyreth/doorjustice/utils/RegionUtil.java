package fr.skyreth.doorjustice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionUtil 
{
	public Region2D deserialize(String reg, String regex) {
    	String[] r = reg.split(regex);
    	World world = Bukkit.getWorld(r[0]);
    	int minX = Integer.parseInt(r[1]);
    	int maxX = Integer.parseInt(r[2]);
    	int minZ = Integer.parseInt(r[3]);
    	int maxZ = Integer.parseInt(r[4]);
    	return new Region2D(world, minX, maxX, minZ, maxZ, true);
    }
	
	public String serializeLocation(Location loc, String sep) {
		return loc.getWorld().getName()+sep+loc.getX()+sep+loc.getY()+sep+loc.getZ()+sep+loc.getYaw()+sep+loc.getPitch();
	}
	
	public String serializeLocationWithoutLook(Location loc, String sep) {
		return loc.getWorld().getName()+sep+loc.getX()+sep+loc.getY()+sep+loc.getZ();
	}
	
	public Location deserializeLocation(String locse, String sep) 
	{
		String[] r = locse.split(",");
		return new Location(Bukkit.getWorld(r[0]), Double.parseDouble(r[1]), Double.parseDouble(r[2]), Double.parseDouble(r[3]));
	}
	
	public Location deserializeLocationWithLook(String locse, String sep) 
	{
		String[] r = locse.split(",");
		return new Location(Bukkit.getWorld(r[0]), Double.parseDouble(r[1]), Double.parseDouble(r[2]), Double.parseDouble(r[3]), Float.parseFloat(r[4]), Float.parseFloat(r[5]));
	}
}
