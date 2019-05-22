package com.mineagepvp.core.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import com.mineagepvp.core.Main;

public class AntiAutoCannon implements Listener {
	
	private Main plugin;
	
	public AntiAutoCannon(Main pl) {
		plugin = pl;
	}
	
	public boolean isRestricted(Material mat) {
		if (mat.equals(Material.SAND) || mat.equals(Material.GRAVEL) || mat.equals(Material.ANVIL)) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void piston(BlockPistonExtendEvent e) {
		for (Block b : e.getBlocks()) {
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int fallingblocks = 0;
			int max = plugin.getConfig().getInt("antiCannonLimit");
			y++;
			while (new Location(b.getWorld(), x, y, z).getBlock().getType().isSolid()) {
				if (isRestricted(new Location(b.getWorld(), x ,y ,z).getBlock().getType())) {
					fallingblocks++;
				}
				if (fallingblocks > max) {
					e.setCancelled(true);
					break;
				}
				y++;
			}
		}
	}
	@EventHandler
	public void pistonr(BlockPistonRetractEvent e) {
		Block b = e.getBlock().getRelative(e.getDirection()).getRelative(BlockFace.UP);
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int fallingblocks = 0;
			int max = plugin.getConfig().getInt("antiCannonLimit");
			while (new Location(b.getWorld(), x, y, z).getBlock().getType().isSolid()) {
				if (isRestricted(new Location(b.getWorld(), x ,y ,z).getBlock().getType())) {
					fallingblocks++;
				}
				if (fallingblocks > max) {
					e.setCancelled(true);
					break;
				}
				y++;
			}
		}
	}


