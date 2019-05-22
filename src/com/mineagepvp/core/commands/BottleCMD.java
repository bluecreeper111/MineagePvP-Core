package com.mineagepvp.core.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import com.mineagepvp.core.api.Api;

public class BottleCMD extends MineageCommand implements Listener {

	public BottleCMD() {
		super("bottle", "mineage.bottle", false);
	}

	public HashSet<UUID> bottleplayers = new HashSet<>();

	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bottleMessageEnable"));
		String message2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bottleMessageDisable"));
		if (bottleplayers.contains(p.getUniqueId())) {
			p.sendMessage(Api.prefix + message2);
			bottleplayers.remove(p.getUniqueId());
			return;
		} else {
			p.sendMessage(Api.prefix + message);
			bottleplayers.add(p.getUniqueId());
			return;
		}
	}

	@EventHandler
	public void onDrink(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if (bottleplayers.contains(p.getUniqueId()) && e.getItem().getType() == Material.POTION) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (ItemStack item : p.getInventory().getContents()) {
						if (item == null) {
							continue;
						}
						if (item.getType() == Material.GLASS_BOTTLE) {
							p.getInventory().remove(item);
							p.updateInventory();
						}
					}
				}
			}, 3L);
		}
	}

}
