package com.mineagepvp.core.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mineagepvp.core.api.Api;

public class FreezeCMD extends MineageCommand implements Listener {
	
	public FreezeCMD() {
		super("freeze", "mineage.freeze", true);
	}
	
    public HashSet<UUID> frozen = new HashSet<>();
	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freezeMessage"));
		String pmessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freezeMessagePlayer"));
		String message2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unfreezeMessage"));
		String pmessage2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unfreezeMessagePlayer"));
		if (args.length > 0) {
			Player tar = Bukkit.getPlayerExact(args[0]);
			if (tar == null) {
				sender.sendMessage(Api.prefix + "§cThat player was not found!");
				return;
			}
			if (!frozen.contains(tar.getUniqueId())) {
				frozen.add(tar.getUniqueId());
				sender.sendMessage(Api.prefix + message.replaceAll("%player%", tar.getName()));
				tar.sendMessage(Api.prefix + pmessage);
				return;
			} else {
				frozen.remove(tar.getUniqueId());
				sender.sendMessage(Api.prefix + message2.replaceAll("%player%", tar.getName()));
				tar.sendMessage(Api.prefix + pmessage2);
				return;
			}
			
		} else {
			sender.sendMessage(Api.prefix + "§cInvalid arguments! /freeze <player>");
			return;
		}
		
	}
	String m = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("currentlyFrozen"));
	@EventHandler
	public void move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (frozen.contains(p.getUniqueId()) && (p.getLocation().getX() != e.getTo().getX() || p.getLocation().getY() != e.getTo().getY()
				|| p.getLocation().getZ() != e.getTo().getZ())) {
			p.teleport(p.getLocation());
			p.sendMessage(Api.prefix + m);
		}
	}
	@EventHandler
	public void run(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (frozen.contains(p.getUniqueId())) {
			e.setCancelled(true);
			p.sendMessage(Api.prefix + m);
		}
	}
	

}
