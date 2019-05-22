package com.mineagepvp.core.commands;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.mineagepvp.core.api.Api;

public class ChunkloaderCMD extends MineageCommand implements Listener {

	public ChunkloaderCMD() {
		super("chunkloader", "mineage.chunkloader.give", true);
	}
	
	public static File chunk = new File("plugins//MineagePvP-Core//chunkloaders.yml");
	public static YamlConfiguration chunks = YamlConfiguration.loadConfiguration(chunk);
	public static HashSet<Location> chunkloaders = new HashSet<>();

	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
			Player tar = Bukkit.getPlayerExact(args[1]);
			if (tar == null) {
				sender.sendMessage(Api.prefix + "§cThat player was not found!");
				return;
			}
			ItemStack hoe = new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
			ItemMeta hoem = hoe.getItemMeta();
			hoem.setDisplayName("§a§lChunkloader");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7Consistently loads the chunk its placed in!");
			lore.add("§c§lTime Left: §f(In Minutes)");
			lore.add("§760");
			hoem.setLore(lore);
			hoe.setItemMeta(hoem);
			tar.getInventory().addItem(hoe);
			sender.sendMessage(Api.prefix + "§aChunkloader given to §2" + tar.getName());
		} else {
			sender.sendMessage(Api.prefix + "§cIncorrect syntax! Try /chunkloader give <player>");
            return;
		}

	}
	
	@EventHandler(ignoreCancelled = true)
	public void use(BlockPlaceEvent e) {
		if (e.getItemInHand().getType() == Material.ENDER_PORTAL_FRAME && e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().getDisplayName().equals("§a§lChunkloader")) {
			Block block = e.getBlock();
			double time = Double.parseDouble(e.getItemInHand().getItemMeta().getLore().get(2).replaceAll("§7", ""));
			block.setMetadata("time", new FixedMetadataValue(plugin, Double.toString(time * 60)));
			block.getState().update();
			chunkloaders.add(block.getLocation());
			e.getPlayer().sendMessage(Api.prefix + "§aPlaced a chunkloader with §2" + time + "§a minutes left!");
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void breakb(BlockBreakEvent e) {
		boolean droptnt = plugin.getConfig().getBoolean("dropChunkloaderBreak");
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		if (!droptnt) { return; }
		if (e.getBlock().hasMetadata("time") && e.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
			List<MetadataValue> md = e.getBlock().getMetadata("time");
			double time = md.get(0).asDouble();
			ItemStack give  = new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
			ItemMeta meta = give.getItemMeta();
			meta.setDisplayName("§a§lChunkloader");
			List<String> lore = new ArrayList<String>();
			lore.add("§7Consistently loads the chunk its placed in!");
			lore.add("§c§lTime Left: §f(In Minutes)");
			lore.add("§7" + df.format(time / 60));
			meta.setLore(lore);
			give.setItemMeta(meta);
			e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), give);
			e.getBlock().setType(Material.AIR);
			chunkloaders.remove(e.getBlock().getLocation());
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void explode(EntityExplodeEvent e) {
		Block remove = null;
		boolean droptnt = plugin.getConfig().getBoolean("dropChunkloaderBreak");
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		if (!droptnt) { return; }
		for (Block block : e.blockList()) {
			if (block.getType() == Material.ENDER_PORTAL_FRAME && block.hasMetadata("time")) {
				Location drop = block.getLocation();
				remove = block;
				double time = block.getMetadata("time").get(0).asDouble();
				ItemStack give  = new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
				ItemMeta meta = give.getItemMeta();
				meta.setDisplayName("§a§lChunkloader");
				List<String> lore = new ArrayList<String>();
				lore.add("§7Consistently loads the chunk its placed in!");
				lore.add("§c§lTime Left: §f(In Minutes)");
				lore.add("§7" + df.format(time / 60));
				meta.setLore(lore);
				give.setItemMeta(meta);
				drop.getWorld().dropItem(drop, give);
				block.setType(Material.AIR);
				chunkloaders.remove(block.getLocation());
			}
		}
		if (remove != null) { e.blockList().remove(remove); }
	}
	@EventHandler
	public void unload(ChunkUnloadEvent e) {
		Chunk chunk = e.getChunk();
		for (Location b : chunkloaders) {
			if (b.getChunk() == chunk) {
				e.setCancelled(true);
			}
		}
	}

}
