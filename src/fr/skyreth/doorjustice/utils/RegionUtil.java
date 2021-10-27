package fr.skyreth.doorjustice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RegionUtil 
{
	public Region2D deserialize(String reg, String regex) {
    	String[] r = reg.split(regex);
    	return new Region2D(Bukkit.getWorld(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]), Integer.parseInt(r[4]));
    }
	
	public String serializeLocation(Location loc, String sep) {
		return loc.getWorld().getName()+sep+loc.getX()+loc.getY()+sep+loc.getZ()+sep+loc.getYaw()+sep+loc.getPitch();
	}
	
	public Location deserializeLocation(String locse, String sep) 
	{
		String[] r = locse.split(",");
		return new Location(Bukkit.getWorld(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]), Integer.parseInt(r[4]), Integer.parseInt(r[5]));
	}
}
