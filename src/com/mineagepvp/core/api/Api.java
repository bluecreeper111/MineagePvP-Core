package com.mineagepvp.core.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import com.mineagepvp.core.commands.MineageCommand;

public class Api {
	
	public static String prefix = ChatColor.translateAlternateColorCodes('&', (MineageCommand.plugin.getConfig().getString("message-prefix") + " &r"));
	
	public static void noPermission(CommandSender p) {
		p.sendMessage(prefix + "§cYou do not have permission to do this!");
		return;
	}
	public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                   blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
	

}
