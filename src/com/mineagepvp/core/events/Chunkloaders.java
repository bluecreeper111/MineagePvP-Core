package com.mineagepvp.core.events;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.mineagepvp.core.Main;
import com.mineagepvp.core.commands.ChunkloaderCMD;

public class Chunkloaders extends BukkitRunnable {
	
    private Main plugin;
    
    public Chunkloaders(Main pl) {
    	plugin = pl;
    }
	
	public void run() {
		for (Iterator<Location> iterator = ChunkloaderCMD.chunkloaders.iterator(); iterator.hasNext();) {
			Location l = iterator.next();
			Block b = l.getBlock();
			double time = b.getMetadata("time").get(0).asDouble();
			if (time - 1 == 0) {
				iterator.remove();
				b.setType(Material.AIR);
				continue;
			}
			b.setMetadata("time", new FixedMetadataValue(plugin, Double.toString(time - 1)));
		}
		
	}
	
	

}
