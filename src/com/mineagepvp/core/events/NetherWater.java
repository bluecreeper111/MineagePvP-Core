package com.mineagepvp.core.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class NetherWater implements Listener {

	@EventHandler
	public void place(PlayerBucketEmptyEvent e) {
		Player p = e.getPlayer();
		if (p.getWorld().getEnvironment() == World.Environment.NETHER
				&& p.getItemInHand().getType() == Material.WATER_BUCKET) {
			e.setCancelled(true);
			e.getBlockClicked().getRelative(e.getBlockFace()).setType(Material.WATER);
			@SuppressWarnings("unused")
			BlockPlaceEvent event = new BlockPlaceEvent(e.getBlockClicked().getRelative(e.getBlockFace()),
					e.getBlockClicked().getRelative(e.getBlockFace()).getState(), e.getBlockClicked(), e.getItemStack(),
					p, false);

		}
	}

}
