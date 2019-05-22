package com.mineagepvp.core.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import com.mineagepvp.core.Main;
import com.mineagepvp.core.api.Api;

public class LavaSponge implements Listener {
	
	private Main plugin;
	
	public LavaSponge(Main pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void onblock(BlockFromToEvent e) {
		int radius = plugin.getConfig().getInt("lavaSpongeRadius");
		Block to = e.getToBlock();
		List<Block> nearby = Api.getNearbyBlocks(to.getLocation(), radius);
		if (!(e.getBlock().getType() == Material.LAVA || e.getBlock().getType() == Material.STATIONARY_LAVA)) { return; }
		for (Block block : nearby) {
			if (block.getType() == Material.SPONGE) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void placeb(PlayerBucketEmptyEvent e) {
		int radius = plugin.getConfig().getInt("lavaSpongeRadius");
		Player p = e.getPlayer();
		ItemStack bucket = p.getItemInHand();
		Location loc = e.getBlockClicked().getLocation();
		Material type = bucket.getType();
		if (type == Material.LAVA_BUCKET) {
			List<Block> nearby = Api.getNearbyBlocks(loc, radius);
			for (Block b : nearby) {
				if (b.getType() == Material.SPONGE) {
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void place(BlockPlaceEvent e) {
		int radius = plugin.getConfig().getInt("lavaSpongeRadius");
		 Block p = e.getBlock();
		 if (p.getType() == Material.SPONGE) {
			 List<Block> nearby = Api.getNearbyBlocks(p.getLocation(), radius);
			 for (Block b : nearby) {
				 if (b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA) {
					 b.setType(Material.AIR);
				 }
			 }
		 }
	}
	

}
