package com.mineagepvp.core.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mineagepvp.core.api.Api;

public class JellylegsCMD extends MineageCommand implements Listener {
	
	public JellylegsCMD() {
		super("jellylegs", "mineage.jellylegs", false);
	}
	
    public HashSet<UUID> jellylegs = new HashSet<>();
	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("jellyLegsMessageEnable"));
		String message2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("jellyLegsMessageDisable"));
		if (jellylegs.contains(p.getUniqueId())) {
			p.sendMessage(Api.prefix + message2);
			jellylegs.remove(p.getUniqueId());
			return;
		} else {
			p.sendMessage(Api.prefix + message);
			jellylegs.add(p.getUniqueId());
			return;
		}
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && jellylegs.contains(((Player)e.getEntity()).getUniqueId())) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		jellylegs.remove(p.getUniqueId());
	}
	

}
